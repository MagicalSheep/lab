package cn.magicalsheep.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

    private static final Logger logger = LogManager.getLogger();
    private static final int port = 8080;

    public static void main(String[] args) throws Exception {
        Utils.loadWords();
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(port));
        try (serverSocket) {
            logger.info("服务端已启动，监听于端口 {}", port);
            while (true) {
                Socket socket = serverSocket.accept();
                logger.info("新客户端使用其端口 {} 连接", socket.getPort());
                new Thread(new Worker(socket)).start();
            }
        }
    }
}