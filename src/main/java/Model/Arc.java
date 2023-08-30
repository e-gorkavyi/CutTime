package Model;

import org.kabeja.dxf.DXFArc;
import org.kabeja.dxf.helpers.Point;

public class Arc implements DXFPrimitive {
    private final DXFArc origin;
    PrimitiveType type = PrimitiveType.ARC;

    public Arc(DXFArc origin) {
        this.origin = origin;
        this.origin.setCounterClockwise(true);
    }

    @Override
    public PrimitiveType getPrimitiveType() {
        return this.type;
    }

    @Override
    public double getX1() {
        return origin.isCounterClockwise() ? origin.getStartPoint().getX() : origin.getEndPoint().getX();
    }

    @Override
    public double getY1() {
        return origin.isCounterClockwise() ? origin.getStartPoint().getY() : origin.getEndPoint().getY();
    }

    @Override
    public double getX2() {
        return origin.isCounterClockwise() ? origin.getEndPoint().getX() : origin.getStartPoint().getX();
    }

    @Override
    public double getY2() {
        return origin.isCounterClockwise() ? origin.getEndPoint().getY() : origin.getStartPoint().getY();
    }

    @Override
    public double getLength() {
        return origin.getLength();
    }

    public boolean isCounterClockwise() {
        return origin.isCounterClockwise();
    }

    public double getRadius() {
        return origin.getRadius();
    }

    public double getStartAngle() {
        return origin.getStartAngle();
    }

    public double getEndAngle() {
        return origin.getEndAngle();
    }

    public Point getCenterPoint() {
        return origin.getCenterPoint();
    }

    public void reverse() {
        origin.setCounterClockwise(!origin.isCounterClockwise());
    }
}
