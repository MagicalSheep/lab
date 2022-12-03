package cn.magicalsheep.client.core;

import cn.magicalsheep.client.Utils;
import cn.magicalsheep.client.ClientMain;
import cn.magicalsheep.common.request.ModeRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import cn.magicalsheep.client.ui.Mode1Frame;
import cn.magicalsheep.common.request.Mode1AnswerRequest;
import cn.magicalsheep.common.response.Mode1Question;
import cn.magicalsheep.common.response.ModeResult;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

import static cn.magicalsheep.common.Utils.ROUND_NUM;

public class Mode1Worker implements Runnable {

    private static final Logger logger = LogManager.getLogger();

    private final JFrame frame;
    private final Mode1Frame layout;

    private int score;

    private boolean timeoutFlag = false;

    public Mode1Worker(JFrame frame, Mode1Frame layout) {
        this.frame = frame;
        this.layout = layout;
    }

    private void sendAnswerRequest(String answer) {
        Mode1AnswerRequest answerRequest = new Mode1AnswerRequest(answer);
        try {
            ClientMain.outputStream.writeObject(answerRequest);
            ClientMain.outputStream.flush();
            logger.info("已发送答案数据包（answer: {}）", answer);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Exception", JOptionPane.PLAIN_MESSAGE);
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); // it will exit the whole application
        }
    }

    private void round() throws Exception {
        // get question packet
        Mode1Question question = (Mode1Question) ClientMain.inputStream.readObject();
        logger.info("已收到问题数据包");
        // update UI
        SwingUtilities.invokeLater(() -> {
            layout.progressBar.setValue(100);
            layout.mean.setText(question.mean());
            layout.msg.setText("");
            layout.confirm.setEnabled(true);
            layout.inputText.setEnabled(true);
            layout.inputText.setText(question.tips());
        });
        // start timer thread
        Thread timer = new Timer(layout.progressBar, percent -> {
            layout.confirm.setEnabled(false);
            layout.inputText.setEnabled(false);
            if (percent <= 0) {
                // timeout request
                timeoutFlag = true;
                sendAnswerRequest(layout.inputText.getText());
            }
        });
        // dynamically register action listener
        for (ActionListener listener : layout.confirm.getListeners(ActionListener.class))
            layout.confirm.removeActionListener(listener);
        layout.confirm.addActionListener(e -> {
            timer.interrupt();
            new Thread(() -> sendAnswerRequest(layout.inputText.getText())).start();
        });
        timeoutFlag = false;
        timer.start();

        // wait for result packet
        ModeResult result = (ModeResult) ClientMain.inputStream.readObject();
        logger.info("已收到结果数据包，结果{}", result.isTrue() ? "正确" : "错误");
        score = result.currentScore();

        if (result.isTrue()) {
            Utils.appendWord(result.answer(), question.mean(), Utils.Status.KNOW);
        } else {
            if (timeoutFlag) Utils.appendWord(result.answer(), question.mean(), Utils.Status.TIMEOUT);
            else Utils.appendWord(result.answer(), question.mean(), Utils.Status.NOT_KNOW);
        }

        // update UI
        SwingUtilities.invokeLater(() -> {
            layout.score.setText("分数：" + result.currentScore());
            layout.mean.setText(result.isTrue() ? "正确" : "错误");
            layout.msg.setText(result.msg());
        });
    }

    @Override
    public void run() {
        try {
            // choose mode 1
            ModeRequest modeRequest = new ModeRequest(1);
            ClientMain.outputStream.writeObject(modeRequest);
            ClientMain.outputStream.flush();

            for (int i = 0; i < ROUND_NUM; i++) {
                logger.info("开始第 {} 轮游戏", i + 1);
                round();
                if (score <= 0) break;
            }
            JOptionPane.showMessageDialog(null, "游戏结束，最后得分：" + score, "游戏结束", JOptionPane.PLAIN_MESSAGE);
            frame.dispose();
            ClientMain.mainFrame.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Exception", JOptionPane.PLAIN_MESSAGE);
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); // it will exit the whole application
        }
    }
}
