package HighScores;

import javax.swing.*;
import java.awt.*;

public class HighScoresRenderer implements ListCellRenderer<String> {
    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel jLabel = new JLabel(value);

        jLabel.setOpaque(true);
        jLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        jLabel.setForeground(Color.WHITE);
        jLabel.setBackground(Color.BLACK);

        return jLabel;
    }
}
