package Model;

import org.kabeja.dxf.DXFLine;
import org.kabeja.dxf.helpers.Point;

public class Line extends DXFPrimitive {
    private final DXFLine origin;
    PrimitiveType type = PrimitiveType.LINE;

    public Line(DXFLine origin) {
        this.origin = origin;
    }

    public Line(double X1, double Y1, double X2, double Y2) {
        this.origin = new DXFLine();
        this.origin.setStartPoint(new Point(X1, Y1, 0));
        this.origin.setEndPoint(new Point(X2, Y2, 0));
    }

    public PrimitiveType getType() {
        return this.type;
    }

    @Override
    public int getID() {
        return origin.getID().equals("") ? 0 : Integer.parseInt(origin.getID(), 16);
    }

    @Override
    public double getX1() {
        return round2dec(origin.getStartPoint().getX());
    }

    @Override
    public double getY1() {
        return round2dec(origin.getStartPoint().getY());
    }

    @Override
    public double getX2() {
        return round2dec(origin.getEndPoint().getX());
    }

    @Override
    public double getY2() {
        return round2dec(origin.getEndPoint().getY());
    }

    public double getLength() {
        return origin.getLength();
    }

    public void reverse() {
        Point tempPoint = origin.getStartPoint();
        origin.setStartPoint(origin.getEndPoint());
        origin.setEndPoint(tempPoint);
    }

    private double lineAngle(Line ln) {
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

    @Override
    int getStartPointAngle() {
        return (int) lineAngle(this);
    }

    @Override
    int getEndPointAngle() {
        return (int) lineAngle(this);
    }

    @Override
    public int compareTo(DXFPrimitive dxfPrimitive) {
        return this.getID() - dxfPrimitive.getID();
    }
}
