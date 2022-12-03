public class Sphere extends Circle {
    public Sphere() {
        super();
    }

    public Sphere(double r) {
        super(r);
    }

    public double getVol() {
        return 4 * Math.PI * Math.pow(getRadius(), 3) / 3;
    }

    public double getArea() {
        return 4 * Math.PI * Math.pow(getRadius(), 2);
    }

    @Override
    public void disp() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "{半径 = " + getRadius() +
                ", 体积 = " + getVol() +
                ", 面积 = " + getArea() + "}";
    }

}
