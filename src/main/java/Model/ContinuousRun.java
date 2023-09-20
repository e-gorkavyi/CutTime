package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public double getRunTime(Map<Integer, Integer> speedsOnRadiuses, PlotterHead head) {
        this.speedCalc(speedsOnRadiuses, head.getSpeed());
        double runTime = 0;
        for (DXFPrimitive primitive : dxfPrimitives) {
            runTime += head.getRunTime(primitive.getStartSpeed(),
                    primitive.getMaxSpeed(),
                    primitive.getEndSpeed(),
                    primitive.getLength(),
                    primitive.getRadius());
        }
        return runTime;
    }

    public double getRunLength() {
        return 0;
    }

    public Point getLastPoint() {
        return dxfPrimitives.get(dxfPrimitives.size() - 1).getEndPoint();
    }

    private double getNearRadiusSpeed(double radius, Map<Integer, Integer> speedsOnRadiuses) {
        double minDiff = Double.MAX_VALUE;
        double nearest = 0;
        for (double key : speedsOnRadiuses.keySet()) {
            double diff = Math.abs(radius - key);
            if (diff < minDiff) {
                nearest = key;
                minDiff = diff;
            }
        }
        return nearest;
    }

    public void speedCalc(Map<Integer, Integer> speedsOnRadiuses, double maxSpeed) {
        for (int i = 0; i < dxfPrimitives.size(); i++) {
            DXFPrimitive primitive = dxfPrimitives.get(i);
            if (i == 0) {
                primitive.setStartSpeed(0);
            }else {
                primitive.setStartSpeed(dxfPrimitives.get(i - 1).getEndSpeed());
            }
            if (primitive.getType().equals(PrimitiveType.LINE)) {
                primitive.setMaxSpeed(maxSpeed);
            } else if (primitive.getType().equals(PrimitiveType.ARC)) {
                primitive.setMaxSpeed(getNearRadiusSpeed(primitive.getRadius(), speedsOnRadiuses));
            }
            if (i == dxfPrimitives.size() - 1) {
                primitive.setEndSpeed(0);
            } else {
                if (dxfPrimitives.get(i + 1).getType().equals(PrimitiveType.ARC)) {
                    primitive.setEndSpeed(getNearRadiusSpeed(dxfPrimitives.get(i + 1).getRadius(), speedsOnRadiuses));
                } else {
                    primitive.setEndSpeed(primitive.getMaxSpeed());
                }
            }
        }
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
