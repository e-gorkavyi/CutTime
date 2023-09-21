package Model;

import Controller.DataRefreshListener;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import org.apache.commons.io.FileUtils;

public class Model {
    private final Map<String, String> data = new HashMap<>() {{
        put("profileName", "");
        put("objectsNum", "");
        put("totalLength", "");
        put("stopsNum", "");
        put("idleRunLength", "");
        put("headUpTime", "");
        put("idleRunTime", "");
        put("workRunTime", "");
        put("totalTime", "");
    }};
    private final ArrayList<DataRefreshListener> listeners = new ArrayList<>();
    private String pathToDxfFiles;

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

    public CalcParameters configParse(String headName) throws IOException, URISyntaxException {
        File file = new File(Model.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        Path basePath = Path.of(file.getParent());
        Path configFile = basePath.resolve("config.ini");
        System.out.println(configFile);
        Map<String, Map<String, String>> config = IniParser.parse(new File(configFile.toString()));
        pathToDxfFiles = config.get("paths").get("input_dir");
        Map<Integer, Integer> confRadiuses = new HashMap<>();
        for (Map.Entry<String, String> entry : config.get("radius_speed").entrySet()) {
            confRadiuses.put(Integer.parseInt(entry.getKey()), Integer.parseInt(entry.getValue()));
        }
        PlotterHead plotterHead = new PlotterHead(
                Integer.parseInt(config.get(headName).get("acceleration")),
                Integer.parseInt(config.get(headName).get("speed")),
                Double.parseDouble(config.get(headName).get("head_up")),
                confRadiuses
        );
        this.data.put("profileName", config.get(headName).get("profile_name"));
        return new CalcParameters(plotterHead, getLastModified(pathToDxfFiles));
    }

    public List<DXFPrimitive> readDXF(CalcParameters calcParameters) throws IOException {
        DXFParser dxfParser = new DXFParser(calcParameters.getDxfFile().getAbsolutePath());
        return dxfParser.getDxfPrimitiveList();
    }

    public void removeFiles() throws IOException {
        FileUtils.cleanDirectory(new File(pathToDxfFiles));
    }

    public void calculate(String headName) throws IOException, URISyntaxException {

//        Request data. Calculation logic.
        CalcParameters calcParameters = configParse(headName);

        List<DXFPrimitive> collection = this.readDXF(calcParameters);

        removeFiles();

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
            if (!run.addPrimitive(primitive)) {
                continuousRuns.add(run.clone());
                Point lastPoint = run.getLastPoint();
                run = new ContinuousRun(calcParameters.getPlotterHead());
                run.setIdleRun(true);
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
        run.setIdleRun(true);
        run.addPrimitive(new Line(
                collection.get(collection.size() - 1).getEndPoint(),
                new Point(0, 0)
        ));
        continuousRuns.add(run);

        // Start/end/max speed for primitives calculation.
        int objectNum = collection.size();
        double totalLength = 0;
        int stopsNum = 0;
        double idleRunLength = 0;
        double headUpTime = 0;
        double workRunTime = 0;
        double idleRunTime = 0;
        for (ContinuousRun continuousRun : continuousRuns) {
            stopsNum++;
            headUpTime += calcParameters.getPlotterHead().getHeadUp();
            if (continuousRun.isIdleRun()) {
                idleRunLength += continuousRun.getRunLength();
                idleRunTime += continuousRun.getRunTime(calcParameters.getPlotterHead().getSpeedsOnRadiuses(),
                        calcParameters.getPlotterHead());
            } else {
                totalLength += continuousRun.getRunLength();
                workRunTime += continuousRun.getRunTime(calcParameters.getPlotterHead().getSpeedsOnRadiuses(),
                        calcParameters.getPlotterHead());
            }
        }


        data.put("objectsNum", String.valueOf(objectNum));
        data.put("totalLength", String.format("%.2f", BigDecimal.valueOf((totalLength / 1000)).setScale(2, RoundingMode.HALF_EVEN).doubleValue()) + " м");
        data.put("stopsNum", String.valueOf(stopsNum));
        data.put("idleRunLength", String.format("%.2f", BigDecimal.valueOf(idleRunLength / 1000).setScale(2, RoundingMode.HALF_EVEN).doubleValue()) + " м");
        data.put("headUpTime", String.format("%.2f", BigDecimal.valueOf(headUpTime / 60).setScale(2, RoundingMode.HALF_EVEN).doubleValue()) + " мин.");
        data.put("idleRunTime", String.format("%.2f", BigDecimal.valueOf(idleRunTime / 60).setScale(2, RoundingMode.HALF_EVEN).doubleValue()) + " мин.");
        data.put("workRunTime", String.format("%.2f", BigDecimal.valueOf(workRunTime / 60).setScale(2, RoundingMode.HALF_EVEN).doubleValue()) + " мин.");
        data.put("totalTime", String.format("%.2f", BigDecimal.valueOf((idleRunTime + workRunTime + headUpTime) / 60).setScale(2, RoundingMode.HALF_EVEN).doubleValue()) + " мин.");

        fireListeners();
    }
}

