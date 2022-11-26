package cn.magicalsheep;

import cn.magicalsheep.ui.MainFrame;
import com.formdev.flatlaf.FlatLightLaf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.info("Flight-ui is starting...");
        FlatLightLaf.setup();
        JFrame frame = new JFrame("Flight-ui");
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                logger.info("Flight-ui is closed");
            }
        });
        frame.setContentPane(new MainFrame().getRootPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1065, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        logger.info("Flight-ui is running");
    }

    public static void exit(int code) {
        logger.info("Flight-ui exited with code {}", code);
        System.exit(code);
    }
}
