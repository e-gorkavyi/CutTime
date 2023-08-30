package Model;

import java.util.Comparator;

public class DxfPrimitiveIdComparator implements Comparator<DXFPrimitive> {
    @Override
    public int compare(DXFPrimitive prim1, DXFPrimitive prim2) {
        return prim1.getID() - prim2.getID();
    }
}
