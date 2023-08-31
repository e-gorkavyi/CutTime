package Model;

import java.util.List;

public class ContinuousRun implements Run, Cloneable{
    private double len;
    private double runTime;
    private Object primitives;

    public ContinuousRun(Object primitives) {
        this.primitives = primitives;
    }

    public double getLen() {
        return len;
    }

    public double getRunTime() {
        return runTime;
    }

    @Override
    public ContinuousRun clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (ContinuousRun) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
