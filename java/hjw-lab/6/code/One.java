import java.util.Random;

public class One {
    public static void main(String[] args) {
        for (int i = 1; i <= 3; i++)
            new Task("线程" + i).start();
    }
}

class Task extends Thread {

    private final Random random = new Random();
    private static final int bound = 1000;

    public Task(String name) {
        this.setName(name);
    }

    @Override
    public void run() {
        System.out.println(this.getName() + "已创建");
        try {
            sleep(random.nextInt(bound) + 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.getName() + "已退出");
    }
}
