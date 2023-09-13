package Model;

public record Point(double x, double y) {
    public boolean coordEqual(Point point, double precision) {
        return Math.abs(x - point.x) <= precision && Math.abs(y - point.y) <= precision;
    }
}
