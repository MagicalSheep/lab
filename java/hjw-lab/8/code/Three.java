import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Three {

    private static final String path = Objects.requireNonNull(Three.class.getClassLoader().getResource(""))
            .getPath().substring(1);
    private static final String separator = System.getProperty("file.separator");
    private static final List<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            File file = new File(URLDecoder.decode(path + separator + "STUDENT.DAT", "utf-8"));
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            for (int i = 1; i <= 5; i++) {
                System.out.println("正在录入第" + i + "个学生的信息");
                System.out.println("请输入学号：");
                int id = scanner.nextInt();
                System.out.println("请输入姓名：");
                String name = scanner.next();
                System.out.println("请输入三科成绩：");
                double score1 = scanner.nextDouble();
                double score2 = scanner.nextDouble();
                double score3 = scanner.nextDouble();
                Student student = new Student(id, name, score1, score2, score3);
                students.add(student);
            }
            out.writeObject(students);
            out.flush();
            out.close();
            System.out.println("\n数据录入成功");
        } catch (FileNotFoundException e) {
            System.err.println("错误：文件STUDENT.DAT不存在！");
        } catch (IOException e) {
            System.err.println("错误：IO异常！");
        }
    }
}