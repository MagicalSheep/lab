import java.util.Scanner;

public class Two {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char[] str = scanner.nextLine().toCharArray();
        int l = 0, r = str.length - 1;
        while (l < r) {
            if (str[l] != str[r]) {
                System.out.println("不是回文串");
                return;
            }
            ++l;
            --r;
        }
        System.out.println("是回文串");
    }
}
