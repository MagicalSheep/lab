public class Main {
    public static void main(String[] args) {
        Student student1 = new Student(1, "测试1", 0, 20, 100);
        Student student2 = new Student(2, "测试2", 0, 20, 100);
        Student student3 = new Student(3, "测试3", 0, 20, 50.5);
        Student student4 = new Student(4, "测试4", 0, 5, -20);
        Student student5 = new Student(5, "测试女生", 1, 53, 40);
        System.out.println(student1);
        System.out.println(student2);
        System.out.println("平均分数：" + (student1.getsJava() + student2.getsJava()) / 2);
        double mx = -0x7fffffff, mn = 0x7fffffff;
        mx = Math.max(mx, student1.getsJava());
        mx = Math.max(mx, student2.getsJava());
        mx = Math.max(mx, student3.getsJava());
        mx = Math.max(mx, student4.getsJava());
        mx = Math.max(mx, student5.getsJava());
        mn = Math.min(mn, student1.getsJava());
        mn = Math.min(mn, student2.getsJava());
        mn = Math.min(mn, student3.getsJava());
        mn = Math.min(mn, student4.getsJava());
        mn = Math.min(mn, student5.getsJava());
        System.out.println("最大值为：" + mx);
        System.out.println("最小值为：" + mn);
    }
}