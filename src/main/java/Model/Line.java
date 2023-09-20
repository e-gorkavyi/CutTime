package Model;

public class Line extends DXFPrimitive {
    private Point startPoint;
    private Point endPoint;
    PrimitiveType type = PrimitiveType.LINE;

    public Line() {
    }

    public Line(Point startPoint, Point endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public PrimitiveType getType() {
        return this.type;
    }

    public double getLength() {
        return Math.sqrt((endPoint.y() - startPoint.y()) * (endPoint.y() - startPoint.y()) +
                (endPoint.x() - startPoint.x()) * (endPoint.x() - startPoint.x()));
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public void reverse() {
        Point tempPoint = getStartPoint();
        startPoint = getEndPoint();
        endPoint = tempPoint;
    }

    private double lineAngle(Line ln) {
        double angle;

        // tan alpha = a / b
        double maxX = Math.max(ln.startPoint.x(), ln.endPoint.x());
        double minX = Math.min(ln.startPoint.x(), ln.endPoint.x());
        double maxY = Math.max(ln.startPoint.y(), ln.endPoint.y());
        double minY = Math.min(ln.startPoint.y(), ln.endPoint.y());
        double a = maxY - minY;
        double b = maxX - minX;
        if (b == 0 && ln.endPoint.y() - ln.startPoint.y() > 0)
            angle = 90;
        else if (b == 0 && ln.endPoint.y() - ln.startPoint.y() < 0)
            angle = 270;
        else if (a == 0 && ln.endPoint.x() - ln.startPoint.x() > 0)
            angle = 0;
        else if (a == 0 && ln.endPoint.x() - ln.startPoint.x() < 0)
            angle = 180;
        else
            angle = Math.toDegrees(Math.atan(a / b));
        if (ln.endPoint.x() - ln.startPoint.x() < 0 && ln.endPoint.y() - ln.startPoint.y() > 0)
            angle = 180 - angle;
        if (ln.endPoint.x() - ln.startPoint.x() < 0 && ln.endPoint.y() - ln.startPoint.y() < 0)
            angle += 180;
        if (ln.endPoint.x() - ln.startPoint.x() > 0 && ln.endPoint.y() - ln.startPoint.y() < 0)
            angle = angle - angle * 2;

        if (angle < 0)
            angle += 360;
        if (angle >= 360)
            angle -= 360;

        return angle;
    }

    @Override
    double getStartPointAngle() {

        return lineAngle(this);
    }

    @Override
    double getEndPointAngle() {

        return lineAngle(this);
    }

    @Override
    double getRadius() {
        return -1;
    }
}
