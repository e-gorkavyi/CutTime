package Model;

import java.util.List;

public class CutRun implements Run {
    private double len;
    private double runTime;

    public CutRun(List<DXFPrimitive> primitives) {

    }

    public double getLen() {
        return len;
    }

    public double getRunTime() {
        return runTime;
    }
}
