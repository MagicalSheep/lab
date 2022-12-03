public class Cylinder extends Circle {
    private final double height;

    public Cylinder(double r, double h) {
        super(r);
        this.height = h;
    }

    public double getHeight() {
        return height;
    }

    public double getVol() {
        return area() * height;
    }

    public double getArea() {
        return 2 * area() + getPerimeter() * height;
    }

    @Override
    public void disp() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "{半径 = " + getRadius() +
                ", 高 = " + height +
                ", 体积 = " + getVol() +
                ", 面积 = " + getArea() + "}";
    }

}
