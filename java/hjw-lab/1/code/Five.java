public class Five {

    private static int func(int x) {
        int ret = 1;
        for (int i = 2; i <= x; i++)
            ret *= i;
        return ret;
    }

    public static void main(String[] args) {
        int sum = 0;
        for (int i = 1; i <= 10; i++)
            sum += func(i);
        System.out.println(sum);
    }
}