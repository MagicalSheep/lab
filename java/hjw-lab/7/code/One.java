import java.util.Locale;
import java.util.Scanner;

public class One {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        String low = str.toLowerCase(Locale.ROOT);
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == low.charAt(i) &&
                    (low.charAt(i) >= 'a' && low.charAt(i) <= 'z'))
                ret.append((char) (low.charAt(i) - 32));
            else ret.append(low.charAt(i));
        }
        System.out.println(ret);
    }
}