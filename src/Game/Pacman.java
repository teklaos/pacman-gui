package Game;

import HighScores.HighScores;
import HighScores.NameInput;
import MainMenu.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Map;

public class Pacman extends Thread {
    private int rowPosition;
    private int columnPosition;
    private int numberOfLives = 2;
    private int updateSpeed = 350;
    private int points = 0;
    private boolean isAlive = true;
    private Direction direction = Direction.NONE;
    private final Controller controller;
    private final Model model;
    private final ImageIcon[] imageIcons;
    private final ImageIcon pointCellIcon;
    private final ImageIcon emptyCellIcon;
    private ImageIcon pacmanCurrentImageIcon;

    public Pacman(int rowPosition, int columnPosition, Controller controller) {
        this.rowPosition = rowPosition;
        this.columnPosition = columnPosition;
        this.controller = controller;
        this.model = controller.getGameModel();

        JTable jTable = controller.getJTable();

        ImageIcon[] imageIcons = new ImageIcon[6];
        imageIcons[0] = getScaledImageIcon(jTable.getRowHeight()/3, jTable.getRowHeight()/3,
                "resources/pacmanDefault.png");
        imageIcons[1] = getScaledImageIcon(jTable.getRowHeight()/3, jTable.getRowHeight()/3,
                "resources/pacmanRight.png");
        imageIcons[2] = getScaledImageIcon(jTable.getRowHeight()/3, jTable.getRowHeight()/3,
                "resources/pacmanLeft.png");
        imageIcons[3] = getScaledImageIcon(jTable.getRowHeight()/3, jTable.getRowHeight()/3,
                "resources/pacmanUp.png");
        imageIcons[4] = getScaledImageIcon(jTable.getRowHeight()/3, jTable.getRowHeight()/3,
                "resources/pacmanDown.png");
        this.imageIcons = imageIcons;

        pointCellIcon = getScaledImageIcon(jTable.getRowHeight()/2, jTable.getRowHeight()/2,
                "resources/point.png");
        emptyCellIcon = getScaledImageIcon(jTable.getRowHeight()/2, jTable.getRowHeight()/2,
                "resources/emptyCell.png");
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public ImageIcon getScaledImageIcon(int width, int height, String filename) {
        Image image = new ImageIcon(filename).getImage();
        Image newImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }

    @Override
    public void run() {
        pacmanCurrentImageIcon = imageIcons[1];
        while (!isInterrupted()) {
            try {
                if (model.isCellPoint(rowPosition, columnPosition))
                    controller.getScore().setText("Score: " + (points += 10));

                if (model.isCellPowerUp(rowPosition, columnPosition) != 0) {
                    switch (model.isCellPowerUp(rowPosition, columnPosition)) {
                        case 1 -> controller.getScore().setText("Score: " + (points += 500));
                        case 2 -> controller.getScore().setText("Score: " + (points += 250));
                        case 3 -> updateSpeed = updateSpeed - updateSpeed /20;
                    }
                    model.setCellPowerUp(rowPosition, columnPosition, 0);
                }

                pacmanAnimation();

                model.setCellIcon(rowPosition, columnPosition, emptyCellIcon);
                model.deleteCellPoint(rowPosition, columnPosition);

                int pointsCounter = 0;
                int numberOfRows = controller.getNumberOfRows();
                int numberOfColumns = controller.getNumberOfColumns();

                for (int i = 0; i < numberOfRows; i++) {
                    for (int j = 0; j < numberOfColumns; j++) {
                        if (model.isCellPoint(i, j))
                            pointsCounter++;
                    }
                }

                if (pointsCounter == 0) {
                    direction = Direction.NONE;
                    rowPosition = 1;
                    columnPosition = 1;
                    pacmanCurrentImageIcon = imageIcons[1];

                    Ghost blinky = controller.getBlinky();
                    Ghost pinky = controller.getPinky();
                    Ghost inky = controller.getInky();

                    int ghostSpeed = blinky.getUpdateSpeed();
                    int newGhostSpeed = ghostSpeed - ghostSpeed/10;

                    Ghost[] ghosts = {blinky, pinky, inky};

                    for (Ghost ghost : ghosts) {
                        model.deleteCellGhost(ghost.getRowPosition(), ghost.getColumnPosition());
                        model.setCellIcon(ghost.getRowPosition(), ghost.getColumnPosition(), emptyCellIcon);
                    }

                    blinky.setPosition(numberOfRows - 2, numberOfColumns - 2);
                    pinky.setPosition(numberOfRows - 2, 1);
                    inky.setPosition(1, numberOfColumns - 2);

                    for (Ghost ghost : ghosts) {
                        model.setCellGhost(ghost.getRowPosition(), ghost.getColumnPosition());
                        ghost.setUpdateSpeed(newGhostSpeed);
                    }

                    for (int i = 0; i < numberOfRows; i++) {
                        for (int j = 0; j < numberOfColumns; j++) {
                            model.setCellPowerUp(i, j, 0);
                            if (!((i == 1 || i == numberOfRows - 2) && ((j >= 1 && j <= 2) ||
                                    (j >= numberOfColumns - 3 && j <= numberOfColumns - 2))) &&
                                    !((i == 2 || i == numberOfRows - 3) && (j == 1 || j == numberOfColumns - 2))) {
                                if (!model.isCellPoint(i, j) && model.isCellNotWall(i, j)) {
                                    model.setCellIcon(i, j, pointCellIcon);
                                    model.setCellPoint(i, j);
                                }
                            }
                        }
                    }
                }

                int nextRowPosition = rowPosition;
                int nextColumnPosition = columnPosition;

                switch (direction) {
                    case RIGHT -> nextColumnPosition++;
                    case LEFT -> nextColumnPosition--;
                    case UP -> nextRowPosition--;
                    case DOWN -> nextRowPosition++;
                }

                switch (direction) {
                    case RIGHT -> {
                        pacmanCurrentImageIcon = imageIcons[1];
                        if (model.isCellNotWall(nextRowPosition, nextColumnPosition))
                            columnPosition++;
                    }
                    case LEFT -> {
                        pacmanCurrentImageIcon = imageIcons[2];
                        if (model.isCellNotWall(nextRowPosition, nextColumnPosition))
                            columnPosition--;
                    }
                    case UP -> {
                        pacmanCurrentImageIcon = imageIcons[3];
                        if (model.isCellNotWall(nextRowPosition, nextColumnPosition))
                            rowPosition--;
                    }
                    case DOWN -> {
                        pacmanCurrentImageIcon = imageIcons[4];
                        if (model.isCellNotWall(nextRowPosition, nextColumnPosition))
                            rowPosition++;
                    }
                }

            } catch (InterruptedException ex) {
                return;
            }
        }
    }

    public void pacmanAnimation() throws InterruptedException {
        checkGhost(rowPosition, columnPosition);
        model.setCellIcon(rowPosition, columnPosition, imageIcons[0]);
        Thread.sleep(updateSpeed /4);
        checkGhost(rowPosition, columnPosition);
        model.setCellIcon(rowPosition, columnPosition, pacmanCurrentImageIcon);
        Thread.sleep(updateSpeed /4);
        checkGhost(rowPosition, columnPosition);
        model.setCellIcon(rowPosition, columnPosition, imageIcons[0]);
        Thread.sleep(updateSpeed /4);
        checkGhost(rowPosition, columnPosition);
        model.setCellIcon(rowPosition, columnPosition, pacmanCurrentImageIcon);
        Thread.sleep(updateSpeed /4);
    }

    public void checkGhost(int rowPosition, int columnPosition) {
        if (model.isCellGhost(rowPosition, columnPosition) && isAlive) {
            if (numberOfLives > 0) {
                direction = Direction.NONE;
                numberOfLives--;
                controller.getView().getLives().remove(numberOfLives);
                controller.getView().getLives().repaint();
                this.rowPosition = 1;
                this.columnPosition = 1;
                pacmanCurrentImageIcon = imageIcons[1];
            }
            else {
                isAlive = false;
                controller.interruptAll();
                try {
                    String username = new NameInput(controller.getView()).getUsername();
                    if (!username.equals("")) {
                        HighScores highScores = new HighScores();
                        try {
                            FileInputStream fileInputStream = new FileInputStream("HighScores.txt");
                            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                            highScores = (HighScores) objectInputStream.readObject();
                        } catch (IOException | ClassNotFoundException ignore) {}

                        boolean newHighestScore = true;
                        for (Map.Entry<String, Integer> entry : highScores.getHighScores().entrySet())
                            if (entry.getKey().equals(username) && entry.getValue() > points) {
                                newHighestScore = false;
                                break;
                            }

                        if (newHighestScore)
                            highScores.addHighScore(username, points);

                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream("HighScores.txt");
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                            objectOutputStream.writeObject(highScores);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                } catch (NullPointerException ignore) {}
                finally {
                    controller.getView().dispose();
                    controller.getPacman().interrupt();
                    SwingUtilities.invokeLater(MainMenu::new);
                }
            }
        }
    }
}
