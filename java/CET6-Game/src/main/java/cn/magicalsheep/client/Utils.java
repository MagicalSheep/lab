package cn.magicalsheep.client;

import java.io.*;

import static cn.magicalsheep.common.Utils.FILE_SEPARATOR;
import static cn.magicalsheep.common.Utils.WORKING_DIR;

public class Utils {

    private static final String knowListFileName = "已掌握单词.txt";

    private static final String notKnowListFileName = "未掌握单词.txt";

    public enum Status {
        NOT_KNOW,
        KNOW,
        TIMEOUT
    }

    public static void appendWord(String word, String mean, Status status) {
        final String listFilePath = switch (status) {
            case KNOW -> WORKING_DIR + FILE_SEPARATOR + knowListFileName;
            case NOT_KNOW, TIMEOUT -> WORKING_DIR + FILE_SEPARATOR + notKnowListFileName;
        };
        File listFile = new File(listFilePath);
        try {
            listFile.createNewFile();
            FileWriter fileWriter = new FileWriter(listFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter, true);
            final String line = switch (status) {
                case KNOW -> word + " " + mean;
                case NOT_KNOW -> "答错 " + word + " " + mean;
                case TIMEOUT -> "未答 " + word + " " + mean;
            };
            printWriter.println(line);
            printWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
