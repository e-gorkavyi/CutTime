package Model;

import Controller.DataRefreshListener;

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

    public List<DXFPrimitive> readDXF(CalcParameters calcParameters) throws IOException {
        DXFParser dxfParser = new DXFParser(calcParameters.getDxfFile().getAbsolutePath());
        return dxfParser.getDxfPrimitiveList();
    }

    public void calculate(String headName) throws IOException {

//        Request data. Calculation logic.
        CalcParameters calcParameters = configParse(headName);

        List<DXFPrimitive> collection = this.readDXF(calcParameters);

        // Passes for reverse wrong-oriented arcs.
        DXFPrimitive current, next;
        for (int i = 0; i < collection.toArray().length - 1; i++) {
            current = collection.get(i);
            next = collection.get(i + 1);
            if (current.getType() == PrimitiveType.ARC) {
                if (current.getStartPoint().coordEqual(next.getStartPoint(), .5) ||
                        current.getEndPoint().coordEqual(next.getEndPoint(), .5) ||
                        current.getStartPoint().coordEqual(next.getEndPoint(), .5)) {
                    current.reverse();
                }
            }
        }

        // Pass for collect primitives into collection of the continuous-run collections
        List<ContinuousRun> continuousRuns = new ArrayList<>();
        ContinuousRun run = new ContinuousRun(calcParameters.getPlotterHead());
        for (DXFPrimitive primitive : collection) {

            System.out.println(primitive.getType() + " " + primitive.getStartPointAngle() + " " + primitive.getEndPointAngle());

            if (!run.addPrimitive(primitive)) {
                continuousRuns.add(run.clone());
                Point lastPoint = run.getLastPoint();
                run = new ContinuousRun(calcParameters.getPlotterHead());
                run.addPrimitive(new Line(
                        lastPoint,
                        primitive.getStartPoint()
                ));
                continuousRuns.add(run.clone());
                run = new ContinuousRun(calcParameters.getPlotterHead());
                run.addPrimitive(primitive);
            }
        }
        continuousRuns.add(run);
        run = new ContinuousRun(calcParameters.getPlotterHead());
        run.addPrimitive(new Line(
                collection.get(collection.size() - 1).getEndPoint(),
                new Point(0, 0)
        ));
        continuousRuns.add(run);

        fireListeners();
    }
}

