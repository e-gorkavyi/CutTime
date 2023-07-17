package Model;
import org.kabeja.dxf.*;
import org.kabeja.parser.ParseException;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        parser.parse("1.dxf");
        DXFDocument document = parser.getDocument();
        DXFLayer layer = document.getDXFLayer("0");
        List<DXFPrimitive> primitives = new ArrayList<>();
        List<DXFEntity> entityList = new ArrayList<>();
        if (layer.hasDXFEntities(DXFConstants.ENTITY_TYPE_LINE))
            entityList.addAll(layer.getDXFEntities(DXFConstants.ENTITY_TYPE_LINE));
        if (layer.hasDXFEntities(DXFConstants.ENTITY_TYPE_ARC))
            entityList.addAll(layer.getDXFEntities(DXFConstants.ENTITY_TYPE_ARC));
        if (layer.hasDXFEntities(DXFConstants.ENTITY_TYPE_CIRCLE))
            entityList.addAll(layer.getDXFEntities(DXFConstants.ENTITY_TYPE_CIRCLE));

        for (int i = 0; i < entityList.size(); i++) {
            Bounds bounds = entityList.get(i).getBounds();
            if (Objects.equals(entityList.get(i).getType(), DXFConstants.ENTITY_TYPE_LINE)) {
                primitives.add(new Line(
                        bounds.getMinimumX(),
                        bounds.getMinimumY(),
                        bounds.getMaximumX(),
                        bounds.getMaximumY())
                );
            }
        }
        primitives.stream().forEach((p) -> System.out.println(
                        p.getPrimitiveType() +
                        ", x1:" + p.getX1() +
                        ", y1:" + p.getY1() +
                        ", x2:" + p.getX2() +
                        ", y2:" + p.getY2()
        ));
    }
}
