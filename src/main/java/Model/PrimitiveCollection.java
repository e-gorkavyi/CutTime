package Model;

import java.util.Comparator;
import java.util.List;

public class PrimitiveCollection {
    private final List<DXFPrimitive> dxfPrimitives;

    public PrimitiveCollection(List<DXFPrimitive> dxfPrimitives) {
        this.dxfPrimitives = dxfPrimitives;
        sort(this.dxfPrimitives);
    }

    public List<DXFPrimitive> getDxfPrimitives() {
        return dxfPrimitives;
    }

    private void sort(List<DXFPrimitive> primitives) {
        primitives.sort(Comparator.comparingInt((DXFPrimitive prim) -> prim.getID()));
    }
}
