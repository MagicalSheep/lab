package cn.magicalsheep.server;

import cn.magicalsheep.server.core.ModeCore;
import cn.magicalsheep.server.core.impl.Mode1Impl;
import cn.magicalsheep.server.core.impl.Mode2Impl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import cn.magicalsheep.common.request.ModeRequest;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Worker implements Runnable {

    private static final Logger logger = LogManager.getLogger();

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public Worker(Socket socket) {
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception ex) {
            // ignored
        }
    }

    @Override
    public void run() {
        if (inputStream == null || outputStream == null) return;
        try {
            // user can use the same socket repeatedly
            while (true) {
                // the first packet choose the mode
                ModeRequest packet = (ModeRequest) inputStream.readObject();
                if (!packet.validate()) return;
                ModeCore modeCore = (packet.mode() == 1) ?
                        new Mode1Impl(inputStream, outputStream) : new Mode2Impl(inputStream, outputStream);
                modeCore.work();
            }
        } catch (SocketException ex) {
            // ignored - user disconnect
            logger.info("客户端断开了连接");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
