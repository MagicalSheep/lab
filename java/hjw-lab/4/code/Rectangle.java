public class Rectangle extends Shape {

    private final double weight;
    private final double height;

    public Rectangle() {
        this.weight = 0;
        this.height = 0;
    }

    public Rectangle(double weight, double height) {
        this.weight = weight;
        this.height = height;
    }

    @Override
    public double getArea() {
        return weight * height;
    }
}
