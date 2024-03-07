package Game;

import javax.swing.*;
import java.awt.*;

public class Ghost extends Thread {
    private int rowPosition;
    private int columnPosition;
    private int updateSpeed = 350;
    private Direction direction = Direction.LEFT;
    private final Model model;
    private final ImageIcon[] imageIcons;
    private final ImageIcon[] powerUpIcons;
    private final ImageIcon[] cellIcons;
    private int droppingPowerUp = 0;

    public Ghost(int rowPosition, int columnPosition, Controller controller) {
        this.rowPosition = rowPosition;
        this.columnPosition = columnPosition;
        this.model = controller.getGameModel();

        int dir = (int)(Math.random() * 5);
        switch (dir) {
            case 1 -> this.direction = Direction.RIGHT;
            case 2 -> this.direction = Direction.LEFT;
            case 3 -> this.direction = Direction.UP;
            case 4 -> this.direction = Direction.DOWN;
        }

        JTable jTable = controller.getJTable();

        ImageIcon[] imageIconsRed = new ImageIcon[8];
        imageIconsRed[0] = getScaledImageIcon(jTable.getRowHeight()/3, jTable.getRowHeight()/3,
                "resources/blinkyRight.png");
        imageIconsRed[1] = getScaledImageIcon(jTable.getRowHeight()/3, jTable.getRowHeight()/3,
                "resources/blinkyRightM.png");
        imageIconsRed[2] = getScaledImageIcon(jTable.getRowHeight()/3, jTable.getRowHeight()/3,
                "resources/blinkyLeft.png");
        imageIconsRed[3] = getScaledImageIcon(jTable.getRowHeight()/3, jTable.getRowHeight()/3,
                "resources/blinkyLeftM.png");
        imageIconsRed[4] = getScaledImageIcon(jTable.getRowHeight()/3, jTable.getRowHeight()/3,
                "resources/blinkyUp.png");
        imageIconsRed[5] = getScaledImageIcon(jTable.getRowHeight()/3, jTable.getRowHeight()/3,
                "resources/blinkyUpM.png");
        imageIconsRed[6] = getScaledImageIcon(jTable.getRowHeight()/3, jTable.getRowHeight()/3,
                "resources/blinkyDown.png");
        imageIconsRed[7] = getScaledImageIcon(jTable.getRowHeight()/3, jTable.getRowHeight()/3,
                "resources/blinkyDownM.png");
        this.imageIcons = imageIconsRed;

        ImageIcon[] powerUpIcons = new ImageIcon[3];
        powerUpIcons[0] = getScaledImageIcon(jTable.getRowHeight()/3, jTable.getRowHeight()/3,
                "resources/cherry.png");
        powerUpIcons[1] = getScaledImageIcon(jTable.getRowHeight()/3, jTable.getRowHeight()/3,
                "resources/strawberry.png");
        powerUpIcons[2] = getScaledImageIcon(jTable.getRowHeight()/3, jTable.getRowHeight()/3,
                "resources/apple.png");
        this.powerUpIcons = powerUpIcons;

        ImageIcon[] cellIcons = new ImageIcon[2];
        cellIcons[0] = getScaledImageIcon(jTable.getRowHeight()/2, jTable.getRowHeight()/2,
                "resources/emptyCell.png");
        cellIcons[1] = getScaledImageIcon(jTable.getRowHeight()/2, jTable.getRowHeight()/2,
                "resources/point.png");
        this.cellIcons = cellIcons;
    }

    public int getRowPosition() {
        return rowPosition;
    }

    public int getColumnPosition() {
        return columnPosition;
    }

    public void setPosition(int x, int y) {
        this.rowPosition = x;
        this.columnPosition = y;
    }

    public int getUpdateSpeed() {
        return updateSpeed;
    }

    public void setUpdateSpeed(int updateSpeed) {
        this.updateSpeed = updateSpeed;
    }

    public void setDroppingPowerUp(int droppingPowerUp) {
        this.droppingPowerUp = droppingPowerUp;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                model.setCellGhost(rowPosition, columnPosition);

                if (direction == Direction.RIGHT)
                    ghostAnimation(0);
                else if (direction == Direction.LEFT)
                    ghostAnimation(2);
                else if (direction == Direction.UP)
                    ghostAnimation(4);
                else if (direction == Direction.DOWN)
                    ghostAnimation(6);


                if (model.isCellPowerUp(rowPosition, columnPosition) != 0) {
                    model.setCellIcon(rowPosition, columnPosition,
                            powerUpIcons[model.isCellPowerUp(rowPosition, columnPosition) - 1]);
                }
                else if (model.isCellPoint(rowPosition, columnPosition))
                    model.setCellIcon(rowPosition, columnPosition, cellIcons[1]);
                else
                    model.setCellIcon(rowPosition, columnPosition, cellIcons[0]);

                if (droppingPowerUp != 0) {
                    model.setCellIcon(rowPosition, columnPosition,
                            powerUpIcons[droppingPowerUp - 1]);
                    model.setCellPowerUp(rowPosition, columnPosition, droppingPowerUp);
                    droppingPowerUp = 0;
                }

                int nextRowPosition = rowPosition;
                int nextColumnPosition = columnPosition;

                switch (direction) {
                    case RIGHT -> nextColumnPosition++;
                    case LEFT -> nextColumnPosition--;
                    case UP -> nextRowPosition--;
                    case DOWN -> nextRowPosition++;
                }

                if (model.isCellWall(nextRowPosition, nextColumnPosition)) {
                    if (direction == Direction.RIGHT || direction == Direction.LEFT) {
                        if (model.isCellNotWall(rowPosition - 1, columnPosition) &&
                                model.isCellNotWall(rowPosition + 1, columnPosition)) {
                            switch ((int)(Math.random() * 2)) {
                                case 0 -> direction = Direction.UP;
                                case 1 -> direction = Direction.DOWN;
                            }
                        }
                        else if (model.isCellNotWall(rowPosition - 1, columnPosition))
                            direction = Direction.UP;
                        else if (model.isCellNotWall(rowPosition + 1, columnPosition))
                            direction = Direction.DOWN;
                        else {
                            if (direction == Direction.RIGHT)
                                direction = Direction.LEFT;
                            else
                                direction = Direction.RIGHT;
                        }
                    }
                    else {
                        if (model.isCellNotWall(rowPosition, columnPosition + 1) &&
                                model.isCellNotWall(rowPosition, columnPosition - 1)) {
                            switch ((int)(Math.random() * 2)) {
                                case 0 -> direction = Direction.RIGHT;
                                case 1 -> direction = Direction.LEFT;
                            }
                        }
                        else if (model.isCellNotWall(rowPosition, columnPosition + 1))
                            direction = Direction.RIGHT;
                        else if (model.isCellNotWall(rowPosition, columnPosition - 1))
                            direction = Direction.LEFT;
                        else {
                            if (direction == Direction.UP)
                                direction = Direction.DOWN;
                            else
                                direction = Direction.UP;
                        }
                    }
                }
                else {
                    if (direction == Direction.RIGHT || direction == Direction.LEFT) {
                        if (model.isCellNotWall(rowPosition - 1, columnPosition)
                                && model.isCellNotWall(rowPosition + 1, columnPosition)) {
                            switch ((int)(Math.random() * 5)) {
                                case 1 -> direction = Direction.UP;
                                case 2 -> direction = Direction.DOWN;
                            }
                        }
                        else if (model.isCellNotWall(rowPosition - 1, columnPosition)) {
                            if ((int) (Math.random() * 5) == 0)
                                direction = Direction.UP;
                        }
                        else if (model.isCellNotWall(rowPosition + 1, columnPosition)) {
                            if ((int) (Math.random() * 5) == 0)
                                direction = Direction.DOWN;
                        }
                    }
                    else {
                        if (model.isCellNotWall(rowPosition, columnPosition + 1)
                                && model.isCellNotWall(rowPosition, columnPosition - 1)) {
                            switch ((int)(Math.random() * 5)) {
                                case 1 -> direction = Direction.RIGHT;
                                case 2 -> direction = Direction.LEFT;
                            }
                        }
                        else if (model.isCellNotWall(rowPosition, columnPosition + 1)) {
                            if ((int) (Math.random() * 5) == 0)
                                direction = Direction.RIGHT;
                        }
                        else if (model.isCellNotWall(rowPosition, columnPosition - 1)) {
                            if ((int) (Math.random() * 5) == 0)
                                direction = Direction.LEFT;
                        }
                    }
                }
                model.deleteCellGhost(rowPosition, columnPosition);
                switch (direction) {
                    case RIGHT -> columnPosition++;
                    case LEFT -> columnPosition--;
                    case UP -> rowPosition--;
                    case DOWN -> rowPosition++;
                }
            } catch (InterruptedException ex) {
                return;
            }
        }
    }

    public void ghostAnimation(int index) throws InterruptedException {
        model.setCellIcon(rowPosition, columnPosition, imageIcons[index]);
        Thread.sleep(updateSpeed /2);
        model.setCellIcon(rowPosition, columnPosition, imageIcons[index + 1]);
        Thread.sleep(updateSpeed /2);
    }

    public ImageIcon getScaledImageIcon(int width, int height, String filename) {
        Image image = new ImageIcon(filename).getImage();
        Image newImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }
}
