package Model;

import java.util.List;

public interface Run {
    default double calcLen(List<DXFPrimitive> primitives) {
        return 0;
    };
    default double calcRunTime(List<DXFPrimitive> primitives, PlotterHead head) {
        return 0;
    };
}
