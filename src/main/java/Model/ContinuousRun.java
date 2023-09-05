package Model;

import org.kabeja.dxf.helpers.Point;

import java.util.ArrayList;
import java.util.List;

public class ContinuousRun implements Run, Cloneable {
    private final List<DXFPrimitive> dxfPrimitives = new ArrayList<>();

    public ContinuousRun(PlotterHead head) {
    }

    public boolean addPrimitive(DXFPrimitive primitive) {
        if (dxfPrimitives.isEmpty()) {
            dxfPrimitives.add(primitive);
            return true;
        } else if (dxfPrimitives.get(dxfPrimitives.size() - 1).getX2() == primitive.getX1() &&
                dxfPrimitives.get(dxfPrimitives.size() - 1).getY2() == primitive.getY1() &&
                dxfPrimitives.get(dxfPrimitives.size() - 1).getEndPointAngle() == primitive.getStartPointAngle()) {
            dxfPrimitives.add(primitive);
            return true;
        } else {
            return false;
        }
    }

    public Point getLastPoint() {
        return new Point(
                dxfPrimitives.get(dxfPrimitives.size() - 1).getX2(),
                dxfPrimitives.get(dxfPrimitives.size() - 1).getY2(),
                0
        );
    }

    @Override
    public ContinuousRun clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (ContinuousRun) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
