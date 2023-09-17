package Model;

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
        } else if (dxfPrimitives.get(dxfPrimitives.size() - 1).pointsAndVectorsEqual(primitive, PointParam.END, PointParam.START)) {
            dxfPrimitives.add(primitive);
            return true;
        } else {
            return false;
        }
    }

    public double getRunTime() {
        return 0;
    }

    public double getRunLength() {
        return 0;
    }

    public Point getLastPoint() {
        return dxfPrimitives.get(dxfPrimitives.size() - 1).getEndPoint();
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
