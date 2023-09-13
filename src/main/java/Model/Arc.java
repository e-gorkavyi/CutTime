package Model;

public class Arc extends DXFPrimitive {
    private Point centerPoint;
    private double radius;
    private double endAngle;
    private double startAngle;
    private boolean counterClockwise;
    PrimitiveType type = PrimitiveType.ARC;

    public Arc() {
    }

    public Arc(
            Point centerPoint,
            double radius,
            double startAngle,
            double endAngle) {
        this.centerPoint = centerPoint;
        this.radius = radius;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
        this.counterClockwise = true;
    }

    public PrimitiveType getType() {
        return this.type;
    }

    public double getLength() {
        double totalAngle = this.getTotalAngle();
        return totalAngle * Math.PI * this.radius / 180.0;
    }

    public double getTotalAngle() {
        return Math.max(this.startAngle, this.endAngle) - Math.min(this.startAngle, this.endAngle);
    }

    public boolean isCounterClockwise() {
        return counterClockwise;
    }

    public void setCounterClockwise(boolean counterClockwise) {
        this.counterClockwise = counterClockwise;
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

    public void setCenterPoint(Point centerPoint) {
        this.centerPoint = centerPoint;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setEndAngle(double endAngle) {
        this.endAngle = endAngle;
    }

    public void setStartAngle(double startAngle) {
        this.startAngle = startAngle;
    }

    public void reverse() {
        setCounterClockwise(!isCounterClockwise());
        double temp = this.startAngle;
        this.startAngle = this.endAngle;
        this.endAngle = temp;
    }

    @Override
    public Point getStartPoint() {
        return new Point(
                centerPoint.x() + radius * Math.cos(Math.toRadians(getStartAngle())),
                centerPoint.y() + radius * Math.sin(Math.toRadians(getStartAngle()))
        );
    }

    @Override
    public Point getEndPoint() {
        return new Point(
                centerPoint.x() + radius * Math.cos(Math.toRadians(getEndAngle())),
                centerPoint.y() + radius * Math.sin(Math.toRadians(getEndAngle()))
        );
    }

    @Override
    double getStartPointAngle() {
        double result;
        if (counterClockwise) {
            result = (this.getStartAngle() + 90);
        } else {
            result = this.getStartAngle() - 90;
        }

        if (round0dec(result) < 0)
            result += 360;
        if (round0dec(result) >= 360)
            result -= 360;

        return result;
    }

    @Override
    double getEndPointAngle() {
        double result;
        if (counterClockwise) {
            result = this.getEndAngle() + 90;
        } else {
            result = this.getEndAngle() - 90;
        }

        if (round0dec(result) < 0)
            result += 360;
        if (round0dec(result) >= 360)
            result -= 360;

        return result;
    }
}
