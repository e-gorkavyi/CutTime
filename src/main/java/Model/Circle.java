package Model;

import org.kabeja.dxf.DXFArc;
import org.kabeja.dxf.DXFCircle;

public class Circle extends Arc {
    private final DXFCircle originCircle;
    private final DXFArc arc = new DXFArc();

    public Circle(DXFCircle origin) {
        super(new DXFArc());
        this.originCircle = origin;
    }

    private void cast(DXFCircle origin) {
        arc.setID(originCircle.getID());
        arc.setCenterPoint(originCircle.getCenterPoint());
        arc.setRadius(originCircle.getRadius());
        arc.setStartAngle(0);
        arc.setEndAngle(360);

        this.origin = arc;
    }
}
