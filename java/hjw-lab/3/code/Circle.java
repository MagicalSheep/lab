public class Circle {

    private final double radius;

    public Circle() {
        radius = 0;
    }

    public Circle(double r) {
        radius = r;
    }

    public double getRadius() {
        return radius;
    }

    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    public double area() {
        return Math.PI * radius * radius;
    }

    public void disp() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "{半径 = " + radius +
                ", 周长 = " + getPerimeter() +
                ", 面积 = " + area() + "}";
    }

}