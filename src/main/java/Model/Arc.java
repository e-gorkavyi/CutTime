package Model;

public class Arc extends DXFPrimitive {
    private final Point centerPoint;
    private final double radius;
    private final double startAngle;
    private final double endAngle;
    private boolean isCounterClockwise;
    PrimitiveType type = PrimitiveType.ARC;

    public Arc(
            Point centerPoint,
            double radius,
            double startAngle,
            double endAngle,
            boolean isCounterClockwise) {
        this.centerPoint = centerPoint;
        this.radius = radius;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
        this.isCounterClockwise = isCounterClockwise;
    }

    public PrimitiveType getType() {
        return this.type;
    }

    public double getLength() {
        double totalAngle = this.getTotalAngle();
        return totalAngle * Math.PI * this.radius / 180.0;
    }

    public double getTotalAngle() {
        return this.endAngle < this.startAngle ?
                360.0 + this.endAngle - this.startAngle :
                Math.abs(this.endAngle - this.startAngle);
    }

    public boolean isCounterClockwise() {
        return isCounterClockwise;
    }

    public void setCounterClockwise(boolean counterClockwise) {
        isCounterClockwise = counterClockwise;
    }

    public double getRadius() {
        return radius;
    }

    public double getStartAngle() {
        return startAngle;
    }

    public double getEndAngle() {
        return endAngle;
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    public void reverse() {
        setCounterClockwise(!isCounterClockwise());
    }

    public Point getStartPoint() {
        double angle = getEndAngle();
        if (isCounterClockwise)
            angle = getStartAngle();
        return new Point(
                centerPoint.x() + radius * Math.cos(angle),
                centerPoint.y() + radius * Math.sin(angle)
        );
    }

    public Point getEndPoint() {
        double angle = getStartAngle();
        if (isCounterClockwise)
            angle = getEndAngle();
        return new Point(
                centerPoint.x() + radius * Math.cos(angle),
                centerPoint.y() + radius * Math.sin(angle)
        );
    }

    @Override
    int getStartPointAngle() {
        int result = 0;
        if (this.isCounterClockwise()) {
            result = (int) round0dec(this.getStartAngle() + 90);
        } else if (!this.isCounterClockwise()) {
            result = (int) round0dec(this.getStartAngle() - 90);
        }
        if (result >= 360)
            result -= 360;
        if (result < 0)
            result += 360;

        return result;
    }

    @Override
    int getEndPointAngle() {
        int result = 0;
        if (this.isCounterClockwise()) {
            result = (int) round0dec(this.getEndAngle() + 90);
        } else if (!this.isCounterClockwise()) {
            result = (int) round0dec(this.getEndAngle() - 90);
        }
        if (result >= 360)
            result -= 360;
        if (result < 0)
            result += 360;

        return result;
    }
}
