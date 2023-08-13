package Model;

import java.util.List;

public class PrimitiveCollection {
    private final List<Line> dxfLines;
    private final List<Arc> dxfArcs;
    private final List<Circle> dxfCircles;

    public PrimitiveCollection(List<Line> dxfLines, List<Arc> dxfArcs, List<Circle> dxfCircles) {
        this.dxfLines = dxfLines;
        this.dxfArcs = dxfArcs;
        this.dxfCircles = dxfCircles;
    }

    public List<Line> getDxfLines() {
        return dxfLines;
    }

    public List<Arc> getDxfArcs() {
        return dxfArcs;
    }

    public List<Circle> getDxfCircles() {
        return dxfCircles;
    }
}
