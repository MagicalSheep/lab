import java.io.*;
import java.net.URLDecoder;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Four {

    private static final String path = Objects.requireNonNull(Four.class.getClassLoader().getResource(""))
            .getPath().substring(1);
    private static final String separator = System.getProperty("file.separator");

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        try {
            File file = new File(URLDecoder.decode(path + separator + "STUDENT.DAT", "utf-8"));
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
            List<Student> list = (List<Student>) input.readObject();
            System.out.println("已录入学生信息：");
            for (Student stu : list)
                System.out.println(stu);
            list.sort(Comparator.comparingDouble(Student::getAverageScore));
            System.out.println("\n平均分最高的学生为：" + list.get(list.size() - 1).getName());
            System.out.println("学生详细信息：");
            System.out.println(list.get(list.size() - 1));
        } catch (FileNotFoundException e) {
            System.err.println("错误：文件STUDENT.DAT不存在！");
        } catch (IOException e) {
            System.err.println("错误：IO异常");
        } catch (ClassNotFoundException e) {
            System.err.println("错误：Student类加载失败，请检查Student.class是否与Four.class处于同一文件夹内");
        }
    }
}
