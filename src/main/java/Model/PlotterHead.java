package Model;

import java.lang.management.ManagementFactory;
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

    public double getRunTime(double startSpeed, double maxSpeed, double endSpeed, double length, double radius) {
        double runTime = 0;
//        int nearRadiusIndex = 0;
//        double maxSpeed;
//        if (radius < 0){
//            maxSpeed = this.speed;
//        } else {
//            for (Map.Entry<Integer, Integer> entry : this.speedsOnRadiuses.entrySet()) {
//                if (Math.abs(radius - entry.getKey()) < nearRadiusIndex)
//                    nearRadiusIndex = Math.abs(nearRadiusIndex - entry.getKey());
//            }
//            maxSpeed = this.speedsOnRadiuses.get(nearRadiusIndex);
//        }

        double accelerationFullDistance = (Math.pow(maxSpeed, 2) - Math.pow(startSpeed, 2)) / (2 * this.acceleration);
        double decelerationFullDistance = (Math.pow(maxSpeed, 2) - Math.pow(endSpeed, 2)) / (2 * this.acceleration);

        if (accelerationFullDistance + decelerationFullDistance > length) {
            double splitDistance = (length / 2) + (Math.pow(endSpeed, 2) / (4 * this.acceleration));
            double splitSpeed = Math.sqrt(2 * acceleration * splitDistance);
            double accelTime = (splitSpeed - startSpeed) / this.acceleration;
            double decelTime = (splitSpeed - endSpeed) / this.acceleration;
            runTime = accelTime + decelTime;
        }else {
            runTime = ((maxSpeed - startSpeed) / this.acceleration) +
                    (length - accelerationFullDistance - decelerationFullDistance) / maxSpeed +
                    ((maxSpeed - endSpeed) / this.acceleration);
        }

        return runTime;
    }
}
