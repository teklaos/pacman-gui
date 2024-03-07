package Game;

import javax.swing.*;

public class Timer extends Thread {
    private final JLabel time;
    private int minutes;
    private int seconds;

    public Timer(JLabel time) {
        this.time = time;
        this.seconds = 0;
        this.minutes = 0;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                Thread.sleep(1000);
                seconds++;

                if (seconds == 60) {
                    minutes++;
                    seconds = 0;
                }

                if (minutes < 10) {
                    if (seconds < 10)
                        time.setText("Time: 0" + minutes + ":0" + seconds);
                    else
                        time.setText("Time: 0" + minutes + ":" + seconds);
                }
                else {
                    if (seconds < 10)
                        time.setText("Time: " + minutes + ":0" + seconds);
                    else
                        time.setText("Time: " + minutes + ":" + seconds);
                }
            } catch (InterruptedException ex) {
                return;
            }
        }
    }
}
