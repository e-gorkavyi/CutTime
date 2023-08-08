package Model;

import org.kabeja.dxf.*;
import org.kabeja.dxf.helpers.Point;
import org.kabeja.parser.ParseException;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Model {

    class Line extends DXFLine implements DXFPrimitive {
        PrimitiveType type = PrimitiveType.LINE;

        public Line() {
        }

        @Override
        public String getType() {
            return this.type.toString();
        }

        @Override
        public PrimitiveType getPrimitiveType() {
            return this.type;
        }

        @Override
        public double getX1() {
            return this.getStartPoint().getX();
        }

        @Override
        public double getY1() {
            return this.getStartPoint().getY();
        }

        @Override
        public double getX2() {
            return this.getEndPoint().getX();
        }

        @Override
        public double getY2() {
            return this.getEndPoint().getY();
        }
    }

    class Arc extends DXFEntity implements DXFPrimitive {
        double
                center_x,
                center_y,
                radius,
                start_angle,
                end_angle,
                length,
                x1,
                y1,
                x2,
                y2;
        PrimitiveType type = PrimitiveType.ARC;

        @Override
        public PrimitiveType getPrimitiveType() {
            return this.type;
        }

        @Override
        public double getX1() {
            return x1;
        }

        @Override
        public double getY1() {
            return y1;
        }

        @Override
        public double getX2() {
            return x2;
        }

        @Override
        public double getY2() {
            return y2;
        }

        @Override
        public Bounds getBounds() {
            return null;
        }

        @Override
        public String getType() {
            return this.type.toString();
        }

        @Override
        public double getLength() {
            return this.length;
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

    public Line arcPoints(Arc iarc) {
        return new Line();
    }

    public void readDXF() throws ParseException {

        Parser parser = ParserBuilder.createDefaultParser();
        parser.parse("111.dxf");
        DXFDocument document = parser.getDocument();
        List<DXFLine> dxfLines = new ArrayList<>();
        List<DXFArc> dxfArcs = new ArrayList<>();
        Iterator<DXFLayer> layerIterator = document.getDXFLayerIterator();
        DXFLayer layer;
        while (layerIterator.hasNext()) {
            layer = layerIterator.next();
            try {
                dxfLines.addAll(layer.getDXFEntities(DXFConstants.ENTITY_TYPE_LINE));
            } catch (Exception ignored) {}
            try {
                dxfArcs.addAll(layer.getDXFEntities(DXFConstants.ENTITY_TYPE_ARC));
            } catch (Exception ignored) {}
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
