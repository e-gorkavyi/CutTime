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

    public Point getStartPoint() {
        return origin.getStartPoint();
    }

    public Model.Point getEndPoint() {
        return origin.getEndPoint();
    }

    public void reverse() {
        Point tempPoint = origin.getStartPoint();
        origin.setStartPoint(origin.getEndPoint());
        origin.setEndPoint(tempPoint);
    }

    private double lineAngle(Line ln) {
        double angle;

        // tan alpha = a / b
        double maxX = Math.max(ln.getX1(), ln.getX2());
        double minX = Math.min(ln.getX1(), ln.getX2());
        double maxY = Math.max(ln.getY1(), ln.getY2());
        double minY = Math.min(ln.getY1(), ln.getY2());
        double a = maxY - minY;
        double b = maxX - minX;
        if (b == 0 && ln.getY2() - ln.getY1() > 0)
            angle = 90;
        else if (b == 0 && ln.getY2() - ln.getY1() < 0)
            angle = 270;
        else if (a == 0 && ln.getX2() - ln.getX1() > 0)
            angle = 0;
        else if (a == 0 && ln.getX2() - ln.getX1() < 0)
            angle = 180;
        else
            angle = Math.toDegrees(Math.atan(a / b));
        if (ln.getX2() - ln.getX1() < 0 && ln.getY2() - ln.getY1() > 0)
            angle += 90;
        if (ln.getX2() - ln.getX1() < 0 && ln.getY2() - ln.getY1() < 0)
            angle += 180;
        if (ln.getX2() - ln.getX1() > 0 && ln.getY2() - ln.getY1() < 0)
            angle += 270;

//        if (ln.getX2() - ln.getX1() != 0) {
//            angle = Math.toDegrees(Math.atan((ln.getY2() - ln.getY1()) / (ln.getX2() - ln.getX1())));
//        } else {
//            angle = -90.0;
//            if (ln.getY2() > ln.getY1())
//                angle = 90.0;
//        }
////        if (ln.getX2() - ln.getX1() < 0 && ln.getY2() - ln.getY1() < 0)
////            angle = angle + 180;
        return angle;
    }

    @Override
    int getStartPointAngle() {
        int result = (int) lineAngle(this);
        if (result >= 360)
            result -= 360;
        if (result < 0)
            result += 360;

        return result;
    }

    @Override
    int getEndPointAngle() {
        int result = (int) lineAngle(this);
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
