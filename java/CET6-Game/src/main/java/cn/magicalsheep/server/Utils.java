package cn.magicalsheep.server;

import cn.magicalsheep.server.model.DictEntry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.magicalsheep.server.model.ChTrans;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static cn.magicalsheep.common.Utils.WORKING_DIR;
import static cn.magicalsheep.common.Utils.FILE_SEPARATOR;

public class Utils {

    public static final List<DictEntry> dict = new ArrayList<>();

    public static void buildDict(InputStream inputStream) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(inputStream);
        for (JsonNode nowEntry : json.get("data")) {
            DictEntry entry = new DictEntry();
            entry.setEnS(nowEntry.get("enS").asText());
            for (JsonNode cur : nowEntry.get("chS")) {
                ChTrans newChTrans = ChTrans.builder()
                        .id(cur.get("id").asInt())
                        .pos(cur.get("pos").asText())
                        .build();
                for (JsonNode NowMean : cur.get("mean"))
                    newChTrans.getMean().add(NowMean.asText());
                entry.getChS().add(newChTrans);
            }
            dict.add(entry);
        }
    }

    public static void loadWords() throws Exception {
        final String jsonFilePath = WORKING_DIR + FILE_SEPARATOR + "CET6-Words.json";
        File wordListJsonFile = new File(jsonFilePath);
        InputStream inputStream = (wordListJsonFile.exists()) ?
                new FileInputStream(wordListJsonFile) : ServerMain.class.getResourceAsStream("/CET6-Words.json");
        try (inputStream) {
            buildDict(inputStream);
        }
    }
}
