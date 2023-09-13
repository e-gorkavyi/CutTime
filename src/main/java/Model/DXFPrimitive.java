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

    protected double round1dec(double value) {
        return BigDecimal.valueOf(value).setScale(1, RoundingMode.HALF_EVEN).doubleValue();
    }

    protected double round0dec(double value) {
        return BigDecimal.valueOf(value).setScale(0, RoundingMode.HALF_EVEN).doubleValue();
    }

    protected boolean isClosely (double a, double b, double accuracy) {
        return Math.max(a, b) - Math.min(a, b) <= accuracy;
    }

    public boolean pointsAndVectorsEqual(DXFPrimitive primitive, PointParam firstParam, PointParam secondParam) {
        double POINT_PRECISION = 0.5;
        double ANGLE_PRECISION = 5;
        if (firstParam.equals(PointParam.START) && secondParam.equals(PointParam.START))
            if (!(this.getStartPoint().coordEqual(primitive.getStartPoint(), POINT_PRECISION) &&
                    isClosely(this.getStartPointAngle(), primitive.getStartPointAngle(), ANGLE_PRECISION))) {
                return false;
            }
        if (firstParam.equals(PointParam.START) && secondParam.equals(PointParam.END))
            if (!(this.getStartPoint().coordEqual(primitive.getEndPoint(), POINT_PRECISION) &&
                    isClosely(this.getStartPointAngle(), primitive.getEndPointAngle(), ANGLE_PRECISION))) {
                return false;
            }
        if (firstParam.equals(PointParam.END) && secondParam.equals(PointParam.START))
            if (!(this.getEndPoint().coordEqual(primitive.getStartPoint(), POINT_PRECISION) &&
                    isClosely(this.getEndPointAngle(), primitive.getStartPointAngle(), ANGLE_PRECISION))) {
                return false;
            }
        if (firstParam.equals(PointParam.END) && secondParam.equals(PointParam.END))
            if (!(this.getEndPoint().coordEqual(primitive.getEndPoint(), POINT_PRECISION) &&
                    isClosely(this.getEndPointAngle(), primitive.getEndPointAngle(), ANGLE_PRECISION))) {
                return false;
            }
        return true;
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

    abstract double getStartPointAngle();

    abstract double getEndPointAngle();

    abstract Point getStartPoint();

    abstract Point getEndPoint();

    abstract double getLength();
}

enum PrimitiveType {
    LINE,
    ARC
}

enum PointParam {
    START,
    END
}