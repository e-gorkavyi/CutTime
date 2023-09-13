package Model;

public class Circle extends Arc {

    public Circle() {}

    public Circle(
            Point centerPoint,
            double radius
    ) {
        super(
                centerPoint,
                radius,
                0,
                350
        );
    }
}
