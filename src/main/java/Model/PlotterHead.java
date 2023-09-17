package Model;

import java.util.Map;

public class PlotterHead {
    private final double acceleration;
    private final double speed;
    private final double headUp;
    private final Map<Integer, Integer> speedsOnRadiuses;


    public PlotterHead(int acceleration, int speed, double headUp, Map<Integer, Integer> speedsOnRadiuses) {
        this.acceleration = acceleration;
        this.speed = speed;
        this.headUp = headUp;
        this.speedsOnRadiuses = speedsOnRadiuses;
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

    public Map<Integer, Integer> getSpeedsOnRadiuses() {
        return speedsOnRadiuses;
    }

    public double getRunTime(double startSpeed, double endSpeed, double length) {
        double accelerationDistance = (acceleration * Math.pow(speed / acceleration, 2)) / 2;
        double decelerationDistance;

        return 0;
    }
}
