package Model;

public class PlotterHead {
    private final double acceleration;
    private final double speed;
    private final double headUp;

    public PlotterHead(int acceleration, int speed, double headUp) {
        this.acceleration = acceleration;
        this.speed = speed;
        this.headUp = headUp;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double getSpeed() {
        return speed;
    }

    public double getHeadUp() {
        return headUp;
    }

    public double getRunTime(double startSpeed, double endSpeed, double length) {
        double accelerationDistance = (acceleration * Math.pow(speed / acceleration, 2)) / 2;
        double decelerationDistance;

        return 0;
    }
}
