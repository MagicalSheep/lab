import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入对象个数：");
        int n = scanner.nextInt();
        for (int i = 1; i <= n; i++) {
            System.out.println("请输入对象类型[1-3]：");
            System.out.println("[1]Circle [2]Sphere [3]Cylinder");
            int in;
            while (true) {
                in = scanner.nextInt();
                if (in >= 1 && in <= 3)
                    break;
                System.out.println("对象类型非法，请重新输入");
            }
            Circle circle;
            if (in == 1) {
                System.out.println("请输入Circle的半径参数：");
                double r = scanner.nextDouble();
                circle = new Circle(r);
            } else if (in == 2) {
                System.out.println("请输入Sphere的半径参数：");
                double r = scanner.nextDouble();
                circle = new Sphere(r);
            } else {
                System.out.println("请输入Cylinder的半径参数：");
                double r = scanner.nextDouble();
                System.out.println("请输入Cylinder的高参数：");
                double h = scanner.nextDouble();
                circle = new Cylinder(r, h);
            }
            circle.disp();
        }
    }
}
