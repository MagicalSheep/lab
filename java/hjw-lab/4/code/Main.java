import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("==========  实验内容1 Begin ==========");
        Shape circle = new Circle(3);
        Shape rectangle = new Rectangle(5, 4);
        System.out.println("Circle的面积为：" + circle.getArea());
        System.out.println("Rectangle的面积为：" + rectangle.getArea());
        System.out.println("==========  实验内容1 End   ==========\n");

        System.out.println("==========  实验内容2 Begin ==========");
        Scanner scanner = new Scanner(System.in);
        Graduate graduate = new Graduate("zhangsan", "男", 20);
        System.out.println("请输入月工资：");
        double pay = scanner.nextDouble();
        graduate.setPay(pay);
        System.out.println("请输入学费：");
        double fee = scanner.nextDouble();
        graduate.setFee(fee);
        if ((graduate.getPay() * 12 - graduate.getFee()) < 2000)
            System.out.println("You need a loan!");
        else
            System.out.println("Your income is enough");
        System.out.println("==========  实验内容2 End   ==========");
    }
}
