package Model;

public class PlotterHead {
    private final int acceleration;
    private final int speed;
    private final double headUp;

    public PlotterHead(int acceleration, int speed, double headUp) {
        this.acceleration = acceleration;
        this.speed = speed;
        this.headUp = headUp;
    }

    public int getAcceleration() {
        return acceleration;
    }

    public int getSpeed() {
        return speed;
    }

    public double getHeadUp() {
        return headUp;
    }
}
