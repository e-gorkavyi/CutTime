package Model;

import org.kabeja.dxf.helpers.Point;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class DXFPrimitive implements Comparable<DXFPrimitive>{
    private double startSpeed;
    private double endSpeed;

    protected double round2dec(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    protected double round0dec(double value) {
        return BigDecimal.valueOf(value).setScale(0, RoundingMode.HALF_EVEN).doubleValue();
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
    abstract Point getStartPoint();
}

enum PrimitiveType {
    LINE,
    ARC;
}