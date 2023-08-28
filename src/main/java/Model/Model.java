package Model;

import Controller.DataRefreshListener;
import org.kabeja.dxf.*;
import org.kabeja.dxf.helpers.Point;
import org.kabeja.parser.ParseException;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.UnaryOperator;

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

    private void fireListeners(int count) {
        for(DataRefreshListener listener : listeners) {
            listener.onDataChanged(count);
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

    public Point[] arcPoints(Arc iarc) {
        double x1 = iarc.getRadius() * Math.cos(Math.toRadians(iarc.getStartAngle()));
        x1 = x1 + iarc.getCenterPoint().getX();

        double y1 = iarc.getRadius() * Math.sin(Math.toRadians(iarc.getStartAngle()));
        y1 = y1 + iarc.getCenterPoint().getY();

        double x2 = iarc.getRadius() * Math.cos(Math.toRadians(iarc.getEndAngle()));
        x2 = x2 + iarc.getCenterPoint().getX();

        double y2 = iarc.getRadius() * Math.sin(Math.toRadians(iarc.getEndAngle()));
        y2 = y2 + iarc.getCenterPoint().getY();

        Point[] result = new Point[2];
        result[0] = new Point(x1, y1, 0);
        result[1] = new Point(x2, y2, 0);

        return result;
    }

    public PrimitiveCollection readDXF(String headName) throws ParseException, IOException {
        CalcParameters calcParameters = configParse(headName);

        Parser parser = ParserBuilder.createDefaultParser();
        parser.parse(calcParameters.getDxfFile().getAbsolutePath());
        DXFDocument document = parser.getDocument();
        List<Line> dxfLines = new ArrayList<>();
        List<Arc> dxfArcs = new ArrayList<>();
        List<Circle> dxfCircles = new ArrayList<>();
        Iterator<DXFLayer> layerIterator = document.getDXFLayerIterator();
        DXFLayer layer;
        while (layerIterator.hasNext()) {
            layer = layerIterator.next();
            try {
                for (Object line : layer.getDXFEntities(DXFConstants.ENTITY_TYPE_LINE)) {
                    dxfLines.add(new Line((DXFLine) line));
                }
//                dxfLines.addAll(layer.getDXFEntities(DXFConstants.ENTITY_TYPE_LINE));
            } catch (Exception ignored) {}
            try {
                for (Object arc : layer.getDXFEntities(DXFConstants.ENTITY_TYPE_ARC)) {
                    dxfArcs.add(new Arc((DXFArc) arc));
                }
//                dxfArcs.addAll(layer.getDXFEntities(DXFConstants.ENTITY_TYPE_ARC));
            } catch (Exception ignored) {}
            try {
                for (Object circle : layer.getDXFEntities(DXFConstants.ENTITY_TYPE_CIRCLE)) {
                    dxfCircles.add(new Circle((DXFArc) circle));
                }
//                dxfCircles.addAll(layer.getDXFEntities(DXFConstants.ENTITY_TYPE_CIRCLE));
            } catch (Exception ignored) {}
        }
        return new PrimitiveCollection(dxfLines, dxfArcs, dxfCircles);
    }

    public void calculate(String headName) throws ParseException, IOException {

//        Request data. Calculation logic.

        PrimitiveCollection collection = this.readDXF(headName);
        for (Arc arc : collection.getDxfArcs()) {
            System.out.println(arc.getStartAngle() + " " + arc.getEndAngle() + " " + arc.getLength());
            arc.reverse();
            System.out.println(arc.getStartAngle() + " " + arc.getEndAngle() + " " + arc.getLength());
        }

        fireListeners(1);
    }
}

