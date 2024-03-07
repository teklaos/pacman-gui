package Game;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class Model extends AbstractTableModel {
    private final Object[][] items;
    private final int[][] powerUps;
    private final boolean[][] ghosts;
    private final boolean[][] points;
    private final boolean[][] walls;

    public Model(int rowIndex, int columnIndex) {
        items = new Object[rowIndex][columnIndex];
        powerUps = new int[rowIndex][columnIndex];
        ghosts = new boolean[rowIndex][columnIndex];
        points = new boolean[rowIndex][columnIndex];
        walls = new boolean[rowIndex][columnIndex];
    }

    public void setCellIcon(int rowIndex, int columnIndex, Icon icon) {
        items[rowIndex][columnIndex] = icon;
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void setCellPowerUp(int rowIndex, int columnIndex, int type) {
        powerUps[rowIndex][columnIndex] = type;
    }

    public int isCellPowerUp(int rowIndex, int columnIndex) {
        return powerUps[rowIndex][columnIndex];
    }

    public void setCellGhost(int rowIndex, int columnIndex) {
        ghosts[rowIndex][columnIndex] = true;
    }

    public void deleteCellGhost(int rowIndex, int columnIndex) {
        ghosts[rowIndex][columnIndex] = false;
    }

    public boolean isCellGhost(int rowIndex, int columnIndex) {
        return ghosts[rowIndex][columnIndex];
    }

    public void setCellPoint(int rowIndex, int columnIndex) {
        points[rowIndex][columnIndex] = true;
    }

    public void deleteCellPoint(int rowIndex, int columnIndex) {
        points[rowIndex][columnIndex] = false;
    }

    public boolean isCellPoint(int rowIndex, int columnIndex) {
        return points[rowIndex][columnIndex];
    }

    public void setCellWall(int rowIndex, int columnIndex) {
        walls[rowIndex][columnIndex] = true;
    }

    public void deleteCellWall(int rowIndex, int columnIndex) {
        walls[rowIndex][columnIndex] = false;
    }

    public boolean isCellWall(int rowIndex, int columnIndex) {
        return walls[rowIndex][columnIndex];
    }

    public boolean isCellNotWall(int rowIndex, int columnIndex) {
        return !walls[rowIndex][columnIndex];
    }

    @Override
    public int getRowCount() {
        return items.length;
    }

    @Override
    public int getColumnCount() {
        return items[0].length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return items[rowIndex][columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }
}
