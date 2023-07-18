package Model;

import org.kabeja.dxf.*;
import org.kabeja.parser.ParseException;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;

import java.util.ArrayList;
import java.util.List;

public class Model{

    class Line extends DXFEntity implements DXFPrimitive {
        double
                x1,
                y1,
                x2,
                y2,
                length;
        PrimitiveType type = PrimitiveType.LINE;

        public Line() {}

        @Override
        public Bounds getBounds() {
            return null;
        }

        @Override
        public String getType() {
            return this.type.toString();
        }

        public Line(double x1, double y1, double x2, double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.length = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        }

        public Line(double x1, double y1, double x2, double y2, double length) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.length = length;
        }

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
        public double getLength() {
            return this.length;
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
        if (ln.x2 - ln.x1 != 0) {
            angle = Math.toDegrees(Math.atan((ln.y2 - ln.y1) / (ln.x2 - ln.x1)));
        } else {
            angle = -90.0;
            if (ln.y2 > ln.y1)
                angle = 90.0;
        }
        if (ln.x2 - ln.x1 < 0 && ln.y2 - ln.y1 < 0)
            angle = angle + 180;
        return angle;
    }

    public Line arcPoints(Arc iarc) {
        return new Line();
    }

    public void readDXF() throws ParseException {
        ArrayList<DXFPrimitive> dxfPrimitives = new ArrayList<>();
        Parser parser = ParserBuilder.createDefaultParser();
        parser.parse("triangle_tuck-lock.dxf");
        DXFDocument document = parser.getDocument();
        DXFLayer layer = document.getDXFLayer("Design");
        List<DXFPrimitive> primitives = new ArrayList<>();
        List<DXFLine> lineList = new ArrayList<>();
        if (layer.hasDXFEntities(DXFConstants.ENTITY_TYPE_LINE))
            lineList.addAll(layer.getDXFEntities(DXFConstants.ENTITY_TYPE_LINE));

        for (int i = 0; i < lineList.size(); i++) {
            primitives.add(new Line(
                    lineList.get(i).getStartPoint().getX(),
                    lineList.get(i).getStartPoint().getY(),
                    lineList.get(i).getEndPoint().getX(),
                    lineList.get(i).getEndPoint().getY(),
                    lineList.get(i).getLength()
            ));
        primitives.forEach((p) -> System.out.println(
                        p.getPrimitiveType() +
                        ", x1:" + p.getX1() +
                        ", y1:" + p.getY1() +
                        ", x2:" + p.getX2() +
                        ", y2:" + p.getY2() +
                        ", lenght:" + p.getLength()
        ));
        }
    }
}
