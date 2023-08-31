package Model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class DXFPrimitive implements Comparable<DXFPrimitive>{
    private double startSpeed;
    private double endSpeed;

    protected double round2dec(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public double getStartSpeed() {
        return startSpeed;
    }

    public void setStartSpeed(double startSpeed) {
        this.startSpeed = startSpeed;
    }

    public double getEndSpeed() {
        return endSpeed;
    }

    public void setEndSpeed(double endSpeed) {
        this.endSpeed = endSpeed;
    }

    abstract int getID();
    abstract double getX1();
    abstract double getY1();
    abstract double getX2();
    abstract double getY2();
    abstract PrimitiveType getType();
    abstract void reverse();
    abstract int getStartPointAngle();
    abstract int getEndPointAngle();
}

enum PrimitiveType {
    LINE,
    ARC;
}