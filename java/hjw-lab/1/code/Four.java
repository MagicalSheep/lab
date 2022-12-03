public class Four {
    public static void main(String[] args) {
        int sum = 0;
        for (int i = 2; i <= 1000; i += 2)
            sum += i;
//        int i = 2;
//        while (i <= 1000) {
//            sum += i;
//            i += 2;
//        }
        System.out.println(sum);
    }
}