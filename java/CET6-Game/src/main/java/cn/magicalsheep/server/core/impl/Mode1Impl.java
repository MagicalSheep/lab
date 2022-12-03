package cn.magicalsheep.server.core.impl;

import cn.magicalsheep.server.core.ModeCore;
import cn.magicalsheep.server.model.DictEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import cn.magicalsheep.common.request.Mode1AnswerRequest;
import cn.magicalsheep.common.response.Mode1Question;
import cn.magicalsheep.common.response.ModeResult;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import static cn.magicalsheep.common.Utils.INTERVAL;
import static cn.magicalsheep.server.Utils.dict;

public class Mode1Impl extends ModeCore {

    private static final Logger logger = LogManager.getLogger();

    public Mode1Impl(ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        super(inputStream, outputStream);
    }

    private ModeResult getResult(DictEntry question, Mode1AnswerRequest answer, long st, long ed) {
        String ans = question.getChS().get(0).getMean().get(0);
        String ansEn = question.getEnS();
        if (ed - st > INTERVAL) {
            return new ModeResult(false, "您没有回答，答案是" + ansEn, --score, ansEn);
        }
        if (!answer.validate()) {
            // unreachable in mode 1
            return new ModeResult(false, "无效数据包", score, ansEn);
        }
        if (ans.equals(answer.word())) {
            return new ModeResult(true, "恭喜回答正确", ++score, ansEn);
        } else {
            score -= 2;
            return new ModeResult(false, "回答错误，答案是" + ansEn, score, ansEn);
        }
    }

    protected void round() throws Exception {
        // random word
        int id = new Random().nextInt(dict.size() + 1);
        DictEntry entry = dict.get(id - 1);

        // build question request
        String mean = entry.getChS().get(0).getMean().get(0);
        String tips = entry.getEnS().substring(0, 1);
        int remain = entry.getEnS().length() - 1;

        Mode1Question question = new Mode1Question(mean, tips, remain);

        outputStream.writeObject(question);
        outputStream.flush();
        long st = System.currentTimeMillis();
        logger.info("已发送问题数据包（timestamp: {}）", st);

        // get answer in some time
        Mode1AnswerRequest answer = (Mode1AnswerRequest) inputStream.readObject();
        long ed = System.currentTimeMillis();
        logger.info("已收到答案数据包（timestamp: {}）", ed);

        // check answer and get result
        ModeResult result = getResult(entry, answer, st, ed);

        outputStream.writeObject(result);
        outputStream.flush();
        logger.info("已发送结果数据包");
    }

}
