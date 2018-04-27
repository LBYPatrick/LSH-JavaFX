package lsh.gui;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import lsh.backend.Core;

public class ProgressBarThread extends Thread {

    ProgressBar bar;
    Core.Status status;

    public ProgressBarThread(ProgressBar bar, Core.Status status) {

        this.bar = bar;
        this.status = status;

    }

    @Override
    public void run() {
        try {

            Platform.runLater(() -> bar.setProgress(0));

            while (status.current < status.total) {
                Platform.runLater(() -> bar.setProgress((((double) status.current) / ((double) status.total))));

                //System.out.printf(status.current + "\t" + status.total + "\n"); //Just for Debug

                Thread.sleep(100);
            }

            Platform.runLater(() -> bar.setProgress(1));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
