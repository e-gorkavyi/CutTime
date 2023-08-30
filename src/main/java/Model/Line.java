package Model;

import org.kabeja.dxf.DXFLine;

public class Line extends DXFLine implements DXFPrimitive {
    private final DXFLine origin;
    PrimitiveType type = PrimitiveType.LINE;

    public Line(DXFLine origin) {
        this.origin = origin;
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
        return origin.getStartPoint().getX();
    }

    @Override
    public double getY1() {
        return origin.getStartPoint().getY();
    }

    @Override
    public double getX2() {
        return origin.getEndPoint().getX();
    }

    @Override
    public double getY2() {
        return origin.getEndPoint().getY();
    }

    public double getLength() {
        return origin.getLength();
    }
}
