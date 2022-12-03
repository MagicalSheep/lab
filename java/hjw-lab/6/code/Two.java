import java.util.Random;

public class Two {
    public static void main(String[] args) {
        new TaskOne("求素数线程").start();
        new TaskTwo("求被3整除数线程").start();
    }
}

class TaskOne extends Thread {

    private final Random random = new Random();
    private static final int bound = 1000;

    public TaskOne(String name) {
        this.setName(name);
    }

    boolean isPrime(int x) {
        if (x == 1) return false;
        for (int i = 2; i * i <= x; i++)
            if (x % i == 0)
                return false;
        return true;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            if (isPrime(i))
                System.out.println("[" + this.getName() + "] " + i + "是素数");
            try {
                sleep(random.nextInt(bound) + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[" + this.getName() + "] " + "计算完毕");
    }
}

class TaskTwo extends Thread {

    private final Random random = new Random();
    private static final int bound = 1000;

    public TaskTwo(String name) {
        this.setName(name);
    }

    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            if (i % 3 == 0)
                System.out.println("[" + this.getName() + "] " + i + "能被3整除");
            try {
                sleep(random.nextInt(bound) + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[" + this.getName() + "] " + "计算完毕");
    }
}
