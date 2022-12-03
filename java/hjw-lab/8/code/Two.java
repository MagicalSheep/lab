import java.io.*;
import java.nio.charset.StandardCharsets;

public class Two {

    private static final File file = new File("C:\\CONFIG.SYS");

    public static void main(String[] args) {
        try {
            if (!file.exists()) {
                System.out.println("文件" + file.getName() + "不存在");
                return;
            }
            System.out.println("文件名称：" + file.getName());
            System.out.println("文件内容：");
            InputStreamReader input = new InputStreamReader(
                    new FileInputStream(file), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(input);
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine())
                System.out.println(line);
            bufferedReader.close();
            input.close();
        } catch (FileNotFoundException e) {
            System.err.println("错误：文件" + file.getName() + "不存在！");
        } catch (IOException e) {
            System.err.println("错误：IO异常！");
        }
    }
}
