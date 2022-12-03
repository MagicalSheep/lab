import java.util.Scanner;

public class Six {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        str = new StringBuilder(str).reverse().toString()
                .replaceAll("\\d{3}(?!$)", "$0,");
        str = new StringBuilder(str).reverse().toString();
        System.out.println(str);
    }
}