package Game;

public class PowerUpDropper extends Thread {
    private final Ghost blinky;
    private final Ghost pinky;
    private final Ghost inky;

    public PowerUpDropper(Ghost blinky, Ghost pinky, Ghost inky) {
        this.blinky = blinky;
        this.pinky = pinky;
        this.inky = inky;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                Thread.sleep(5000);
                if ((int) (Math.random() * 4) == 0) {
                    int powerUpType = ((int)(Math.random() * 3) + 1);
                    switch ((int) (Math.random() * 3)) {
                        case 0 -> blinky.setDroppingPowerUp(powerUpType);
                        case 1 -> pinky.setDroppingPowerUp(powerUpType);
                        case 2 -> inky.setDroppingPowerUp(powerUpType);
                    }
                }
            } catch (InterruptedException ex) {
                return;
            }
        }
    }
}
