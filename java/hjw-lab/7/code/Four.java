import java.util.Scanner;

public class Four {
    public static void main(String[] args) {
        boolean[] vis = new boolean[256];
        Scanner scanner = new Scanner(System.in);
        char[] str = scanner.nextLine().toCharArray();
        StringBuilder ret = new StringBuilder();
        for (char c : str) {
            if (!vis[c])
                ret.append(c);
            vis[c] = true;
        }
        System.out.println(ret);
    }
}
