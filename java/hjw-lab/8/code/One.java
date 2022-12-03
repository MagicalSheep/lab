import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class One {

    private static final String path = Objects.requireNonNull(One.class.getClassLoader().getResource(""))
            .getPath().substring(1);
    private static final String separator = System.getProperty("file.separator");

    private static boolean isPrime(int x) {
        if (x == 1) return false;
        for (int i = 2; i * i <= x; i++)
            if (x % i == 0)
                return false;
        return true;
    }

    public static void main(String[] args) {
        try {
            File file = new File(URLDecoder.decode(path + separator + "PRIME.DAT", "utf-8"));
            OutputStreamWriter out = new OutputStreamWriter(
                    new FileOutputStream(file), StandardCharsets.UTF_8);
            BufferedWriter bufferedWriter = new BufferedWriter(out);
            for (int i = 2; i <= 200; i++) {
                if (isPrime(i)) {
                    bufferedWriter.write(String.valueOf(i));
                    bufferedWriter.newLine();
                }
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            out.close();
        } catch (FileNotFoundException e) {
            System.err.println("错误：文件PRIME.DAT不存在！");
        } catch (IOException e) {
            System.err.println("错误：IO异常！");
        }
    }
}
