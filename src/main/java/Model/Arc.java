package Model;

import org.kabeja.dxf.DXFArc;
import org.kabeja.dxf.helpers.Point;

public class Arc extends DXFPrimitive {
    private final DXFArc origin;
    PrimitiveType type = PrimitiveType.ARC;

    public Arc(DXFArc origin) {
        this.origin = origin;
        this.origin.setCounterClockwise(true);
    }

    public PrimitiveType getType() {
        return this.type;
    }

    @Override
    public double getX1() {
        return origin.isCounterClockwise() ? round2dec(origin.getStartPoint().getX()) : round2dec(origin.getEndPoint().getX());
    }

    @Override
    public double getY1() {
        return origin.isCounterClockwise() ? round2dec(origin.getStartPoint().getY()) : round2dec(origin.getEndPoint().getY());
    }

    @Override
    public double getX2() {
        return origin.isCounterClockwise() ? round2dec(origin.getEndPoint().getX()) : round2dec(origin.getStartPoint().getX());
    }

    @Override
    public double getY2() {
        return origin.isCounterClockwise() ? round2dec(origin.getEndPoint().getY()) : round2dec(origin.getStartPoint().getY());
    }

    public double getLength() {
        return origin.getLength();
    }

    public int getID() {
        return Integer.parseInt(origin.getID(), 16);
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

    @Override
    public int compareTo(DXFPrimitive dxfPrimitive) {
        return this.getID() - dxfPrimitive.getID();
    }
}
