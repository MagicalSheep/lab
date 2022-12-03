package cn.magicalsheep.client.core;

import javax.swing.*;

import static cn.magicalsheep.common.Utils.INTERVAL_CLIENT;

public class Timer extends Thread {

    public interface Callback {
        void run(double percent);
    }

    private final JProgressBar progressBar;

    private final Callback callback;

    public Timer(JProgressBar progressBar, Callback callback) {
        this.progressBar = progressBar;
        this.callback = callback;
    }

    @Override
    public void run() {
        long st = System.currentTimeMillis();
        double percent = 100;
        while (!isInterrupted()) {
            long cur = System.currentTimeMillis();
            percent = (int) (Math.max(0, 100.0 * (INTERVAL_CLIENT - cur + st) / INTERVAL_CLIENT));
            int finalPercent = (int) percent;
            SwingUtilities.invokeLater(() -> progressBar.setValue(finalPercent));
            if (percent <= 0) break;
            try {
                Thread.sleep(1000); // reduce CPU busy time
            } catch (InterruptedException e) {
                break;
            }
        }
        callback.run(percent);
    }
}
