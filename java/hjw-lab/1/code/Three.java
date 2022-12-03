import java.util.Scanner;

public class Three {

    private static double func(double weight, double val) {
        if (val < 100)
            return 1.0 * weight * val;
        else if (val >= 100 && val < 300)
            return 0.9 * weight * val;
        else if (val >= 300 && val < 500)
            return 0.8 * weight * val;
        else if (val >= 500 && val < 1000)
            return 0.7 * weight * val;
        else
            return 0.6 * weight * val;
    }

    public static void main(String[] args) {
        double weight, val;
        Scanner scanner = new Scanner(System.in);
        weight = scanner.nextDouble();
        val = scanner.nextDouble();
        System.out.println(func(weight, val));
    }
}