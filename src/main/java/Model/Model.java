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
    private ArrayList<DataRefreshListener> listeners = new ArrayList<>();

    public static File getLastModified(String directoryFilePath) {
        System.out.println(System.getProperty("file.encoding"));

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

    public Map<String, String> getData() {
        return data;
    }

    public void addListener(DataRefreshListener listener) {
        listeners.add(listener);
        System.out.println(listeners);
    }

    public void removeListener(DataRefreshListener listener) {
        listeners.remove(listener);
    }

    private void fireListeners() {
        for (DataRefreshListener listener : listeners) {
            listener.onDataChanged();
        }
    }

    public double angleDif(double angle1, double angle2) {
        double dif = Math.abs(angle1 - angle2);
        if (dif > 180)
            dif = 360 - dif;
        return dif;
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

    public List<DXFPrimitive> readDXF(CalcParameters calcParameters) throws ParseException, IOException {

        DXFParser dxfParser = new DXFParser(calcParameters.getDxfFile().getAbsolutePath());

        Parser parser = ParserBuilder.createDefaultParser();
        parser.parse(calcParameters.getDxfFile().getAbsolutePath());
        DXFDocument document = parser.getDocument();
        List<DXFPrimitive> dxfPrimitives = new ArrayList<>();
        List<DXFPrimitive> dxfLines = new ArrayList<>();
        List<DXFPrimitive> dxfArcs = new ArrayList<>();
        Iterator<DXFLayer> layerIterator = document.getDXFLayerIterator();
        DXFLayer layer;
        while (layerIterator.hasNext()) {
            layer = layerIterator.next();
            try {
                for (Object circle : layer.getDXFEntities(DXFConstants.ENTITY_TYPE_CIRCLE)) {
                    dxfPrimitives.add(new Circle((DXFCircle) circle));
                }
            } catch (Exception ignored) {
            }
            try {
                for (Object line : layer.getDXFEntities(DXFConstants.ENTITY_TYPE_LINE)) {
                    dxfLines.add(new Line((DXFLine) line));
                }
            } catch (Exception ignored) {
            }
            try {
                for (Object arc : layer.getDXFEntities(DXFConstants.ENTITY_TYPE_ARC)) {
                    dxfArcs.add(new Arc((DXFArc) arc));
                }
            } catch (Exception ignored) {
            }
        }

        Iterator<DXFPrimitive> dxfLinesIterator = dxfLines.listIterator();
        DXFPrimitive line;
        DXFPrimitive arc;
        while (dxfLinesIterator.hasNext()) {
            line = dxfLinesIterator.next();
            dxfPrimitives.add(line);
            Iterator<DXFPrimitive> dxfArcsIterator = dxfArcs.listIterator();
            while (dxfArcsIterator.hasNext()) {
                arc = dxfArcsIterator.next();
                if (line.getEndPoint().equals(arc.getStartPoint()) ||
                        line.getEndPoint().equals(arc.getEndPoint())) {
                    dxfPrimitives.add(arc);
                    dxfArcsIterator.remove();
                    break;
                }
            }
        }
        dxfPrimitives.addAll(dxfArcs);

        return dxfPrimitives;
    }

    public void calculate(String headName) throws ParseException, IOException {

//        Request data. Calculation logic.
        CalcParameters calcParameters = configParse(headName);

        List<DXFPrimitive> collection = this.readDXF(calcParameters);
        collection.sort(Comparator.comparingInt((DXFPrimitive prim) -> prim.getID()));

        // Pass for reverse wrong-oriented arcs.
        DXFPrimitive current, next;
        for (int i = 0; i < collection.toArray().length - 1; i++) {
            current = collection.get(i);
            next = collection.get(i + 1);
            if (current.getType() == PrimitiveType.ARC)
                if (current.getStartPoint().equals(next.getStartPoint()))
                    current.reverse();
            if (next.getType() == PrimitiveType.ARC)
                if (current.getEndPoint().equals(next.getEndPoint()))
                    next.reverse();
        }

        // Pass for collect primitives into collection of the continuous-run collections
        List<ContinuousRun> continuousRuns = new ArrayList<>();
        ContinuousRun run = new ContinuousRun(calcParameters.getPlotterHead());
        DXFLine idleRunLine = new DXFLine();
        for (DXFPrimitive primitive : collection) {
            System.out.println(
                    primitive.getClass().getName() +
                            " " +
                            primitive.getX1() +
                            " " +
                            primitive.getY1() +
                            " " +
                            primitive.getStartPointAngle() +
                            " " +
                            primitive.getX2() +
                            " " +
                            primitive.getY2() +
                            " " +
                            primitive.getEndPointAngle()
            );
            if (!run.addPrimitive(primitive)) {
                continuousRuns.add(run.clone());
                idleRunLine.setStartPoint(run.getLastPoint());
                idleRunLine.setEndPoint(primitive.getStartPoint());
                run = new ContinuousRun(calcParameters.getPlotterHead());
                run.addPrimitive(new Line(idleRunLine));
                run = new ContinuousRun(calcParameters.getPlotterHead());
                run.addPrimitive(primitive);
            }
        }
        continuousRuns.add(run);
        run = new ContinuousRun(calcParameters.getPlotterHead());
        idleRunLine.setStartPoint(
                new Point(
                        collection.get(collection.size() - 1).getX2(),
                        collection.get(collection.size() - 1).getY2(),
                        0));
        idleRunLine.setEndPoint(new Point(0, 0, 0));
        run.addPrimitive(new Line(idleRunLine));
        continuousRuns.add(run);

        fireListeners();
    }
}

