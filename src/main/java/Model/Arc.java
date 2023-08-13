package Model;

import org.kabeja.dxf.DXFArc;

public class Arc extends DXFArc implements DXFPrimitive {

    PrimitiveType type = PrimitiveType.ARC;

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

    @Override
    public String getType() {
        return this.type.toString();
    }
}
