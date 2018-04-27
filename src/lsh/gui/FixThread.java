package lsh.gui;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import lsh.backend.Core;

public class FixThread extends Thread {

    private int fixNumber;
    private  ProgressBar bar;
    public Core.Status stat;

    public FixThread(int fixNumber, ProgressBar bar) {
        this.fixNumber = fixNumber;
        this.bar = bar;
        this.stat = new Core.Status();
    }

    @Override
    public void run() {
        new Thread(() -> {
            try {
                Platform.runLater(() -> bar.setStyle("-fx-accent:blue "));
                Platform.runLater(() -> bar.setProgress(0));

                while (stat.current < stat.total) {
                    Platform.runLater(() -> bar.setProgress((((double) stat.current) / ((double) stat.total))));

                    //System.out.printf(status.current + "\t" + status.total + "\n"); //Just for Debug

                    Thread.sleep(100); //10 Times per second -- Can't be too fast
                }

                Platform.runLater(() -> bar.setProgress(1));
                Platform.runLater(() -> bar.setStyle("-fx-accent: green "));

            } catch(Exception e) {
                e.printStackTrace();
            }
        }).start();

        new ProgressBarThread(bar,stat).start();
        Core.RunFix(fixNumber,stat);
    }

    public synchronized Core.Status getStatus() {
        return this.stat;
    }
}
