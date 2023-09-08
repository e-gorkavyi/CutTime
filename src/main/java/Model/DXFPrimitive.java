package Model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class DXFPrimitive {
    private double startSpeed;
    private double maxSpeed;
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

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getEndSpeed() {
        return endSpeed;
    }

    public void setEndSpeed(double endSpeed) {
        this.endSpeed = endSpeed;
    }

    abstract PrimitiveType getType();
    abstract void reverse();
    abstract int getStartPointAngle();
    abstract int getEndPointAngle();
    abstract Point getStartPoint();
    abstract Point getEndPoint();
}

enum PrimitiveType {
    LINE,
    ARC;
}