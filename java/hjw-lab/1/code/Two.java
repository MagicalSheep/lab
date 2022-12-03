import java.util.Scanner;

public class Two {
    public static void main(String[] args) {
        int x;
        Scanner scanner = new Scanner(System.in);
        x = scanner.nextInt();
        if (x < 0 || x > 9999) {
            System.out.println("输入的数字范围应为[0, 9999]");
            return;
        }
        if (x < 10)
            System.out.println("这个数是一位数");
        else if (x < 100)
            System.out.println("这个数是两位数");
        else if (x < 1000)
            System.out.println("这个数是三位数");
        else
            System.out.println("这个数是四位数");
    }
}