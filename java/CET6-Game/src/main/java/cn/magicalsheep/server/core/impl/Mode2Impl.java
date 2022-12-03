package cn.magicalsheep.server.core.impl;

import cn.magicalsheep.server.model.DictEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import cn.magicalsheep.common.request.Mode2AnswerRequest;
import cn.magicalsheep.common.response.ModeResult;
import cn.magicalsheep.common.response.Mode2Question;
import cn.magicalsheep.server.core.ModeCore;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static cn.magicalsheep.common.Utils.INTERVAL;
import static cn.magicalsheep.server.Utils.dict;

public class Mode2Impl extends ModeCore {

    private static final Logger logger = LogManager.getLogger();

    public Mode2Impl(ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        super(inputStream, outputStream);
    }

    private ModeResult getResult(DictEntry question, Mode2Question rawQuestion,
                                 Mode2AnswerRequest answer, long st, long ed) {
        int ansIndex = 0;
        for (; ansIndex < rawQuestion.options().size(); ansIndex++) {
            String str = rawQuestion.options().get(ansIndex);
            if (str.equals(question.getChS().get(0).getMean().get(0))) break;
        }
        String ans = rawQuestion.options().get(ansIndex);
        ++ansIndex; // [1, 4]

        if (ed - st > INTERVAL) {
            return new ModeResult(false, "您没有回答，答案是" + ans, --score, ans);
        }
        if (!answer.validate()) {
            return new ModeResult(false, "无效数据包", score, ans);
        }
        if (answer.answer() == ansIndex) {
            return new ModeResult(true, "恭喜回答正确", ++score, ans);
        } else {
            score -= 2;
            return new ModeResult(false, "回答错误，答案是" + ans, score, ans);
        }
    }

    protected void round() throws Exception {
        // random word
        int id = new Random().nextInt(dict.size() + 1);
        DictEntry entry = dict.get(id - 1);

        // build question request
        String word = entry.getEnS();
        List<String> options = new ArrayList<>();
        options.add(entry.getChS().get(0).getMean().get(0));
        for (int i = 0; i < 3; i++) {
            int tmpId = new Random().nextInt(dict.size() + 1);
            DictEntry tmpEntry = dict.get(tmpId - 1);
            options.add(tmpEntry.getChS().get(0).getMean().get(0));
        }
        Collections.shuffle(options);

        Mode2Question question = new Mode2Question(word, options);

        outputStream.writeObject(question);
        outputStream.flush();
        long st = System.currentTimeMillis();
        logger.info("已发送问题数据包（timestamp: {}）", st);

        // get answer in some time
        Mode2AnswerRequest answer = (Mode2AnswerRequest) inputStream.readObject();
        long ed = System.currentTimeMillis();
        logger.info("已收到答案数据包（timestamp: {}）", ed);

        // check answer and get result
        ModeResult result = getResult(entry, question, answer, st, ed);

        outputStream.writeObject(result);
        outputStream.flush();
        logger.info("已发送结果数据包");
    }
}
