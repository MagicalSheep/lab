import java.util.Scanner;

public class One {
    public static void main(String[] args) {
        int x, y, z;
        Scanner scanner = new Scanner(System.in);
        x = scanner.nextInt();
        y = scanner.nextInt();
        z = scanner.nextInt();
        x *= x;
        y *= y;
        z *= z;
        if ((x + y == z) || (x + z == y) || (y + z == x))
            System.out.println("是直角三角形");
        else
            System.out.println("不是直角三角形");
    }
}