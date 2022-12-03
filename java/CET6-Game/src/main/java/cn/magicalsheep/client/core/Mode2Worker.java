package cn.magicalsheep.client.core;

import cn.magicalsheep.client.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import cn.magicalsheep.client.ClientMain;
import cn.magicalsheep.client.ui.Mode2Frame;
import cn.magicalsheep.common.request.Mode2AnswerRequest;
import cn.magicalsheep.common.request.ModeRequest;
import cn.magicalsheep.common.response.Mode2Question;
import cn.magicalsheep.common.response.ModeResult;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

import static cn.magicalsheep.common.Utils.ROUND_NUM;

public class Mode2Worker implements Runnable {

    private static final Logger logger = LogManager.getLogger();

    private final JFrame frame;
    private final Mode2Frame layout;

    private int score;

    private boolean timeoutFlag = false;

    private class DyListener implements ActionListener {

        private final Thread timer;
        private final int option;

        public DyListener(Thread timer, int option) {
            this.timer = timer;
            this.option = option;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            timer.interrupt();
            new Thread(() -> sendAnswerRequest(option)).start();
        }
    }

    public Mode2Worker(JFrame frame, Mode2Frame layout) {
        this.frame = frame;
        this.layout = layout;
    }

    private void sendAnswerRequest(int answer) {
        Mode2AnswerRequest answerRequest = new Mode2AnswerRequest(answer);
        try {
            ClientMain.outputStream.writeObject(answerRequest);
            ClientMain.outputStream.flush();
            if (answer == 5)
                logger.info("超时，已发送无效数据包");
            else
                logger.info("已发送答案数据包（answer: {}）", answer);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Exception", JOptionPane.PLAIN_MESSAGE);
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); // it will exit the whole application
        }
    }

    private void round() throws Exception {
        // get question packet
        Mode2Question question = (Mode2Question) ClientMain.inputStream.readObject();
        logger.info("已收到问题数据包");
        // update UI
        SwingUtilities.invokeLater(() -> {
            layout.progressBar.setValue(100);
            layout.word.setText(question.word());
            layout.msg.setText("");
            layout.aButton.setEnabled(true);
            layout.bButton.setEnabled(true);
            layout.cButton.setEnabled(true);
            layout.dButton.setEnabled(true);
            layout.aButton.setText("A. " + question.options().get(0));
            layout.bButton.setText("B. " + question.options().get(1));
            layout.cButton.setText("C. " + question.options().get(2));
            layout.dButton.setText("D. " + question.options().get(3));
        });
        // start timer thread
        Thread timer = new Timer(layout.progressBar, percent -> {
            layout.aButton.setEnabled(false);
            layout.bButton.setEnabled(false);
            layout.cButton.setEnabled(false);
            layout.dButton.setEnabled(false);
            if (percent <= 0) {
                // timeout request
                timeoutFlag = true;
                sendAnswerRequest(5);
            }
        });
        // dynamically register action listener
        for (ActionListener listener : layout.aButton.getListeners(ActionListener.class))
            layout.aButton.removeActionListener(listener);
        for (ActionListener listener : layout.bButton.getListeners(ActionListener.class))
            layout.bButton.removeActionListener(listener);
        for (ActionListener listener : layout.cButton.getListeners(ActionListener.class))
            layout.cButton.removeActionListener(listener);
        for (ActionListener listener : layout.dButton.getListeners(ActionListener.class))
            layout.dButton.removeActionListener(listener);
        layout.aButton.addActionListener(new DyListener(timer, 1));
        layout.bButton.addActionListener(new DyListener(timer, 2));
        layout.cButton.addActionListener(new DyListener(timer, 3));
        layout.dButton.addActionListener(new DyListener(timer, 4));
        timeoutFlag = false;
        timer.start();

        // wait for result packet
        ModeResult result = (ModeResult) ClientMain.inputStream.readObject();
        logger.info("已收到结果数据包，结果{}", result.isTrue() ? "正确" : "错误");
        score = result.currentScore();

        if (result.isTrue()) {
            Utils.appendWord(question.word(), result.answer(), Utils.Status.KNOW);
        } else {
            if (timeoutFlag) Utils.appendWord(question.word(), result.answer(), Utils.Status.TIMEOUT);
            else Utils.appendWord(question.word(), result.answer(), Utils.Status.NOT_KNOW);
        }

        // update UI
        SwingUtilities.invokeLater(() -> {
            layout.score.setText("分数：" + result.currentScore());
            layout.word.setText(result.isTrue() ? "正确" : "错误");
            layout.msg.setText(result.msg());
        });
    }

    @Override
    public void run() {
        try {
            // choose mode 2
            ModeRequest modeRequest = new ModeRequest(2);
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
