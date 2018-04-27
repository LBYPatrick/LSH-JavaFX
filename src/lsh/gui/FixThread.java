package lsh.gui;

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
        new ProgressBarThread(bar,stat).start();
        Core.RunFix(fixNumber,stat);
    }

    public synchronized Core.Status getStatus() {
        return this.stat;
    }
}
