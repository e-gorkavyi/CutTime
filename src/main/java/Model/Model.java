package Model;

import Controller.DataRefreshListener;
import org.kabeja.dxf.*;
import org.kabeja.parser.ParseException;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Model {
    private Map<String, String> data = new HashMap<>() {{
        put("objectsNum", "");
        put("totalLength", "");
        put("stopsNum", "");
        put("idleRunLength", "");
        put("headUpTime", "");
        put("idleRunTime", "");
        put("workRunTime", "");
        put("totalTime", "");
    }};

    public Map<String, String> getData() {
        return data;
    }

    private ArrayList<DataRefreshListener> listeners = new ArrayList<>();

    public void addListener(DataRefreshListener listener) {
        listeners.add(listener);
        System.out.println(listeners);
    }

    public void removeListener(DataRefreshListener listener) {
        listeners.remove(listener);
    }

    private void fireListeners() {
        for(DataRefreshListener listener : listeners) {
            listener.onDataChanged();
        }
    }

    public double lineAngle(Line ln) {
        double angle;
        if (ln.getX2() - ln.getX1() != 0) {
            angle = Math.toDegrees(Math.atan((ln.getY2() - ln.getY1()) / (ln.getX2() - ln.getX1())));
        } else {
            angle = -90.0;
            if (ln.getY2() > ln.getY1())
                angle = 90.0;
        }
        if (ln.getX2() - ln.getX1() < 0 && ln.getY2() - ln.getY1() < 0)
            angle = angle + 180;
        return angle;
    }

    public double angleDif(double angle1, double angle2) {
        double dif = Math.abs(angle1 - angle2);
        if (dif > 180)
            dif = 360 - dif;
        return dif;
    }

    public static File getLastModified(String directoryFilePath) {
        File directory = new File(directoryFilePath);
        File[] files = directory.listFiles(File::isFile);
        long lastModifiedTime = Long.MIN_VALUE;
        File chosenFile = null;
        if (files != null) {
            for (File file : files) {
                if (file.lastModified() > lastModifiedTime) {
                    chosenFile = file;
                    lastModifiedTime = file.lastModified();
                }
            }
        }
        return chosenFile;
    }

    public CalcParameters configParse(String headName) throws IOException {
        Map<String, Map<String, String>> config = IniParser.parse(new File("config.ini"));
        String pathToDxfFiles = config.get("paths").get("input_dir");
        PlotterHead plotterHead = new PlotterHead(
                Integer.parseInt(config.get(headName).get("acceleration")),
                Integer.parseInt(config.get(headName).get("speed")),
                Double.parseDouble(config.get(headName).get("head_up"))
        );
        return new CalcParameters(plotterHead, getLastModified(pathToDxfFiles));
    }

    public List<DXFPrimitive> readDXF(String headName) throws ParseException, IOException {
        CalcParameters calcParameters = configParse(headName);

        Parser parser = ParserBuilder.createDefaultParser();
        parser.parse(calcParameters.getDxfFile().getAbsolutePath());
        DXFDocument document = parser.getDocument();
        List<DXFPrimitive> dxfPrimitives = new ArrayList<>();
        Iterator<DXFLayer> layerIterator = document.getDXFLayerIterator();
        DXFLayer layer;
        while (layerIterator.hasNext()) {
            layer = layerIterator.next();
            try {
                for (Object line : layer.getDXFEntities(DXFConstants.ENTITY_TYPE_LINE)) {
                    dxfPrimitives.add(new Line((DXFLine) line));
                }
            } catch (Exception ignored) {}
            try {
                for (Object arc : layer.getDXFEntities(DXFConstants.ENTITY_TYPE_ARC)) {
                    dxfPrimitives.add(new Arc((DXFArc) arc));
                }
            } catch (Exception ignored) {}
            try {
                for (Object circle : layer.getDXFEntities(DXFConstants.ENTITY_TYPE_CIRCLE)) {
                    dxfPrimitives.add(new Circle((DXFArc) circle));
                }
            } catch (Exception ignored) {}
        }
        return dxfPrimitives;
    }

    public void calculate(String headName) throws ParseException, IOException {

//        Request data. Calculation logic.

        List<DXFPrimitive> collection = this.readDXF(headName);
        collection.sort(Comparator.comparingInt((DXFPrimitive prim) -> prim.getID()));

        DXFPrimitive current, next;
        for (int i = 0; i < collection.toArray().length - 1; i++) {
            current = collection.get(i);
            next = collection.get(i + 1);
            if (current.getType() == PrimitiveType.ARC)
                if (current.getX1() == next.getX1() && current.getY1() == next.getY1())
                    current.reverse();
            if (next.getType() == PrimitiveType.ARC)
                if (current.getX2() == next.getX2() && current.getY2() == next.getY2())
                    next.reverse();
        }

        fireListeners();
    }
}

