package Model;

import org.kabeja.dxf.DXFLine;
import org.kabeja.dxf.helpers.Point;

public class Line extends DXFPrimitive {
    private final DXFLine origin;
    PrimitiveType type = PrimitiveType.LINE;

    public Line(DXFLine origin) {
        this.origin = origin;
    }

    public PrimitiveType getType() {
        return this.type;
    }

    @Override
    int getID() {
        return Integer.parseInt(origin.getID(), 16);
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

    @Override
    public int compareTo(DXFPrimitive dxfPrimitive) {
        return this.getID() - dxfPrimitive.getID();
    }
}
