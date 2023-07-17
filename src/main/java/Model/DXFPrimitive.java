package Model;

import org.kabeja.dxf.DXFEntity;

public interface DXFPrimitive {
    PrimitiveType getPrimitiveType();
    double getX1();
    double getY1();
    double getX2();
    double getY2();
    double getLength();
}

enum PrimitiveType {
    LINE,
    ARC;
}