import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Person person = new Person();
        System.out.println("请输入Person姓名：");
        String name = scanner.nextLine();
        person.setName(name);
        System.out.println("请输入Person性别（男/女）：");
        String sex = scanner.nextLine();
        person.setSex(sex);
        System.out.println("请输入Person年龄：");
        int age = scanner.nextInt();
        try {
            person.setAge(age);
            System.out.println(person);
        } catch (IllegalAgeException e) {
            System.out.println(e.getMessage());
        }
    }
}
