import java.util.Scanner;

public class Five {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        String dest = scanner.nextLine();
        int ans = 0, st = 0;
        while (true) {
            st = str.indexOf(dest, st);
            if (st == -1)
                break;
            st += dest.length();
            ++ans;
        }
        System.out.println(ans);
    }
}