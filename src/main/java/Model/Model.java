package Model;

import org.kabeja.dxf.*;
import org.kabeja.parser.ParseException;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Model {
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

        //find last DXF file in the directory


        return new CalcParameters(plotterHead, getLastModified(pathToDxfFiles));
    }

    public Line arcPoints(Arc iarc) {
        return new Line();
    }

    public void readDXF(String headName) throws ParseException, IOException {
        CalcParameters calcParameters = configParse(headName);

        Parser parser = ParserBuilder.createDefaultParser();
        parser.parse(calcParameters.getDxfFile().getAbsolutePath());
        DXFDocument document = parser.getDocument();
        List<DXFLine> dxfLines = new ArrayList<>();
        List<DXFArc> dxfArcs = new ArrayList<>();
        Iterator<DXFLayer> layerIterator = document.getDXFLayerIterator();
        DXFLayer layer;
        while (layerIterator.hasNext()) {
            layer = layerIterator.next();
            try {
                dxfLines.addAll(layer.getDXFEntities(DXFConstants.ENTITY_TYPE_LINE));
            } catch (Exception ignored) {
            }
            try {
                dxfArcs.addAll(layer.getDXFEntities(DXFConstants.ENTITY_TYPE_ARC));
            } catch (Exception ignored) {
            }
        }

        for (DXFLine line : dxfLines) {
            System.out.println(line.getLength() +
                    " " +
                    line.getStartPoint().toString() +
                    " " +
                    line.getEndPoint().toString());
        }

        for (DXFArc arc : dxfArcs) {
            System.out.println(arc.getStartPoint().getX() +
                    "/" +
                    arc.getStartPoint().getY() +
                    " " +
                    arc.getEndPoint().getX() +
                    "/" +
                    arc.getEndPoint().getY() +
                    " " +
                    arc.getCenterPoint().getX() +
                    " " +
                    arc.getCenterPoint().getY() +
                    " " +
                    arc.getRadius());
        }
    }
}

