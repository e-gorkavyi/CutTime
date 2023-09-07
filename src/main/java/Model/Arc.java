package Model;

import org.kabeja.dxf.DXFArc;
import org.kabeja.dxf.helpers.Point;

public class Arc extends DXFPrimitive {
    DXFArc origin;
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
        return origin.getID().equals("") ? 0 : Integer.parseInt(origin.getID(), 16);
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

    public Point getStartPoint() {
        return origin.getStartPoint();
    }

    public Point getEndPoint() {
        return origin.getEndPoint();
    }

    @Override
    int getStartPointAngle() {
        int result = 0;
        if (this.isCounterClockwise()) {
            result = (int) round0dec(this.getStartAngle() + 90);
        } else if (!this.isCounterClockwise()) {
            result = (int) round0dec(this.getStartAngle() - 90);
        }
        if (result >= 360)
            result -= 360;
        if (result < 0)
            result += 360;

        return result;
//        return this.isCounterClockwise() ?
//                (int) round0dec(this.getStartAngle() + 90) >= 360 ?
//                        (int) round0dec(this.getStartAngle() + 90) - 360 :
//                        (int) round0dec(this.getStartAngle() + 90) :
//                (int) round0dec(this.getStartAngle() - 90) >= 360 ?
//                        (int) round0dec(this.getStartAngle() - 90) - 360 :
//                        (int) round0dec(this.getStartAngle() - 90);
    }

    @Override
    int getEndPointAngle() {
        int result = 0;
        if (this.isCounterClockwise()) {
            result = (int) round0dec(this.getEndAngle() + 90);
        } else if (!this.isCounterClockwise()) {
            result = (int) round0dec(this.getEndAngle() - 90);
        }
        if (result >= 360)
            result -= 360;
        if (result < 0)
            result += 360;

        return result;
    }

    @Override
    public int compareTo(DXFPrimitive dxfPrimitive) {
        return this.getID() - dxfPrimitive.getID();
    }
}
