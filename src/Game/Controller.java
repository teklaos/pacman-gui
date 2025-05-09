package Game;

import MainMenu.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller {
    private final View view;
    private final JTable jTable;
    private final int numberOfRows;
    private final int numberOfColumns;
    private final Model model;
    private final JLabel score;
    private final Timer timer;
    private final Pacman pacman;
    private final Ghost blinky;
    private final Ghost pinky;
    private final Ghost inky;
    private final PowerUpDropper powerUpDropper;

    public Controller(int numberOfRows, int numberOfColumns) {
        view = new View();

        view.setSize(numberOfColumns * 25, numberOfRows * 25 + 85);
        view.setLocationRelativeTo(null);
        view.setVisible(true);

        this.jTable = view.getJTable();
        this.score = view.getScore();

        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;

        ImageIcon lifeIcon = getScaledImageIcon(20, 20, "resources/pacmanRight.png");

        view.getFirstLife().setIcon(lifeIcon);
        view.getSecondLife().setIcon(lifeIcon);

        model = new Model(numberOfRows, numberOfColumns);
        jTable.setModel(model);

        // Map Generation

        ImageIcon wallImageIcon = getScaledImageIcon(100, 100, "resources/wall.png");
        ImageIcon pointImageIcon = getScaledImageIcon(jTable.getRowHeight()/2, jTable.getRowHeight()/2,
                "resources/point.png");
        ImageIcon emptyCellImageIcon = getScaledImageIcon(jTable.getRowHeight()/2, jTable.getRowHeight()/2,
                "resources/emptyCell.png");

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                model.setCellIcon(i, j, wallImageIcon);
                model.setCellWall(i, j);
            }
        }

        for (int i = 1; i < numberOfRows - 1; i++) {
            for (int j = 1; j < numberOfColumns - 1; j++) {
                if (i == 1 || i == numberOfRows - 2 || j == 1 || j == numberOfColumns - 2) {
                    model.setCellIcon(i, j, pointImageIcon);
                    model.deleteCellWall(i, j);
                    model.setCellPoint(i, j);
                }
                if (numberOfColumns % 2 == 1) {
                    model.setCellIcon(i, numberOfColumns/2, pointImageIcon);
                    model.deleteCellWall(i, numberOfColumns/2);
                    model.setCellPoint(i, numberOfColumns/2);
                }
                else {
                    model.setCellIcon(i, numberOfColumns/4, pointImageIcon);
                    model.deleteCellWall(i, numberOfColumns/4);
                    model.setCellPoint(i, numberOfColumns/4);
                    model.setCellIcon(i, numberOfColumns - numberOfColumns/4 - 1, pointImageIcon);
                    model.deleteCellWall(i, numberOfColumns - numberOfColumns/4 - 1);
                    model.setCellPoint(i, numberOfColumns - numberOfColumns/4 - 1);
                }
            }
        }

        int percentage = 100;
        while (percentage > 55) {
            int x1 = (int)(Math.random() * (numberOfRows - 2)) + 1;
            int y1 = (int)(Math.random() * (numberOfColumns - 2)) + 1;
            int x2 = (int)(Math.random() * (numberOfRows - 2)) + 1;
            int y2 = (int)(Math.random() * (numberOfColumns - 2)) + 1;

            model.setCellIcon(x1, y1, pointImageIcon);
            model.setCellIcon(x2, y2, pointImageIcon);
            model.deleteCellWall(x1, y1);
            model.deleteCellWall(x2, y2);
            model.setCellPoint(x1, y1);
            model.setCellPoint(x2, y2);

            Direction[] possibleDirections = new Direction[2];

            int dx = 0;
            int dy = 0;

            if (x1 > x2) {
                possibleDirections[0] = Direction.UP;
                dx = x1 - x2;
            }
            else if (x1 < x2) {
                possibleDirections[0] = Direction.DOWN;
                dx = x2 - x1;
            }

            if (y1 > y2) {
                possibleDirections[1] = Direction.LEFT;
                dy = y1 - y2;
            }
            else if (y1 < y2) {
                possibleDirections[1] = Direction.RIGHT;
                dy = y2 - y1;
            }

            int opt;
            int steps = dx + dy;

            for (int i = 0; i < steps; i++) {
                if (dx > 0 && dy > 0) {
                    opt = (int)(Math.random() * 2);
                    switch (possibleDirections[opt]) {
                        case RIGHT -> {
                            y1++;
                            dy--;
                        }
                        case LEFT -> {
                            y1--;
                            dy--;
                        }
                        case UP -> {
                            x1--;
                            dx--;
                        }
                        case DOWN -> {
                            x1++;
                            dx--;
                        }
                    }
                }
                else if (dx > 0) {
                    switch (possibleDirections[0]) {
                        case UP -> {
                            x1--;
                            dx--;
                        }
                        case DOWN -> {
                            x1++;
                            dx--;
                        }
                    }
                }
                else if (dy > 0) {
                    switch (possibleDirections[1]) {
                        case RIGHT -> {
                            y1++;
                            dy--;
                        }
                        case LEFT -> {
                            y1--;
                            dy--;
                        }
                    }
                }
                model.setCellIcon(x1, y1, pointImageIcon);
                model.deleteCellWall(x1, y1);
                model.setCellPoint(x1, y1);
            }

            int wallCounter = 0;
            for (int i = 0; i < numberOfRows; i++){
                for (int j = 0; j < numberOfColumns; j++) {
                    if (model.isCellWall(i, j))
                        wallCounter++;
                }
            }
            percentage = (int)(((double)wallCounter/(double)(numberOfRows * numberOfColumns)) * 100);
        }

        // Spawn Cells

        for (int i = 0; i < numberOfRows - 1; i++) {
            for (int j = 0; j < numberOfColumns - 1; j++) {
                if (((i == 1 || i == numberOfRows - 2) && ((j >= 1 && j <= 2) ||
                        (j >= numberOfColumns - 3 && j <= numberOfColumns - 2))) ||
                        ((i == 2 || i == numberOfRows - 3) && (j == 1 || j == numberOfColumns - 2))) {
                    model.setCellIcon(i, j, emptyCellImageIcon);
                    model.deleteCellWall(i, j);
                    model.deleteCellPoint(i, j);
                }
            }
        }

        // Start of The Game

        pacman = new Pacman(1,1, this);
        pacman.start();

        timer = new Timer(view.getTime());
        timer.start();

        blinky = new Ghost(numberOfRows - 2, numberOfColumns - 2, this);
        blinky.start();

        pinky = new Ghost(numberOfRows - 2, 1, this);
        pinky.start();

        inky = new Ghost(1, numberOfColumns - 2, this);
        inky.start();

        powerUpDropper = new PowerUpDropper(blinky, pinky, inky);
        powerUpDropper.start();

        jTable.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT ||
                        e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    e.consume();
                }
                if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Q) {
                    interruptAll();
                    view.dispose();
                    pacman.interrupt();
                    SwingUtilities.invokeLater(MainMenu::new);
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) pacman.setDirection(Direction.RIGHT);
                else if (e.getKeyCode() == KeyEvent.VK_LEFT) pacman.setDirection(Direction.LEFT);
                else if (e.getKeyCode() == KeyEvent.VK_UP) pacman.setDirection(Direction.UP);
                else if (e.getKeyCode() == KeyEvent.VK_DOWN) pacman.setDirection(Direction.DOWN);
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });

        jTable.setBackground(Color.BLACK);
        jTable.setIntercellSpacing(new Dimension(0, 0));
        jTable.setCellSelectionEnabled(false);
    }

    public View getView() {
        return view;
    }

    public JTable getJTable() {
        return jTable;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public Model getGameModel() {
        return model;
    }

    public JLabel getScore() {
        return score;
    }

    public ImageIcon getScaledImageIcon(int width, int height, String filename) {
        Image image = new ImageIcon(filename).getImage();
        Image newImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }

    public Pacman getPacman() {
        return pacman;
    }

    public Ghost getBlinky() {
        return blinky;
    }

    public Ghost getPinky() {
        return pinky;
    }

    public Ghost getInky() {
        return inky;
    }

    public void interruptAll() {
        timer.interrupt();
        blinky.interrupt();
        pinky.interrupt();
        inky.interrupt();
        powerUpDropper.interrupt();
    }
}
