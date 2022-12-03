package cn.magicalsheep.server.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static cn.magicalsheep.common.Utils.ROUND_NUM;

public abstract class ModeCore {

    private static final Logger logger = LogManager.getLogger();
    protected final ObjectInputStream inputStream;
    protected final ObjectOutputStream outputStream;

    protected int score = 10;

    public ModeCore(ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    protected abstract void round() throws Exception;

    public void work() throws Exception {
        // ROUND_NUM rounds totally
        for (int i = 0; i < ROUND_NUM; i++) {
            logger.info("开始第 {} 轮游戏", i + 1);
            round();
            if (score <= 0) break; // end of the game
            Thread.sleep(2000);
        }
    }

}
