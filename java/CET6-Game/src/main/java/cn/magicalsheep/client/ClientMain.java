package cn.magicalsheep.client;

import com.formdev.flatlaf.FlatLightLaf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import cn.magicalsheep.client.ui.MainFrame;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientMain {

    private static final Logger logger = LogManager.getLogger();
    private static final String addr = "127.0.0.1";
    private static final int port = 8080;

    public static final JFrame mainFrame = new JFrame("Client");

    public static Socket socket;
    public static ObjectInputStream inputStream;
    public static ObjectOutputStream outputStream;

    public static void main(String[] args) {
        FlatLightLaf.setup();
        try {
            socket = new Socket(addr, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            logger.info("连接服务端成功");

            mainFrame.setContentPane(new MainFrame().root);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.pack();
            mainFrame.setSize(370, 370);
            mainFrame.setResizable(false);
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Exception", JOptionPane.PLAIN_MESSAGE);
        }
    }
}
