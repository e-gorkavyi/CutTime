package Model;

import org.kabeja.dxf.DXFLine;

public class Line extends DXFLine implements DXFPrimitive {
    PrimitiveType type = PrimitiveType.LINE;

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
