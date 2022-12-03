public class Circle extends Shape {

    private final double radius;

    public Circle() {
        this.radius = 0;
    }

    public Circle(double r) {
        this.radius = r;
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
}
