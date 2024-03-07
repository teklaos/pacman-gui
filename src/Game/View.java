package Game;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame {
    private final JTable jTable;
    private final JLabel score;
    private final JLabel time;
    private final JPanel lives;
    private final JLabel firstLife;
    private final JLabel secondLife;

    public View() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());

        JPanel stats = new JPanel();
        stats.setLayout(new FlowLayout(FlowLayout.CENTER));

        score = new JLabel("Score: -");
        score.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        score.setBackground(Color.BLACK);
        score.setForeground(Color.WHITE);

        time = new JLabel("Time: 00:00");
        time.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        time.setBackground(Color.BLACK);
        time.setForeground(Color.WHITE);

        stats.setOpaque(true);
        stats.add(score);
        stats.add(time);
        stats.setBackground(Color.BLACK);

        lives = new JPanel();
        lives.setLayout(new FlowLayout(FlowLayout.LEFT));

        firstLife = new JLabel();
        secondLife = new JLabel();

        lives.add(firstLife);
        lives.add(secondLife);

        lives.setOpaque(true);
        lives.setBackground(Color.BLACK);

        jTable = new JTable() {
            @Override
            public int getRowHeight() {
                return this.getColumnModel().getColumn(0).getWidth();
            }
        };

        jPanel.add(stats, BorderLayout.PAGE_START);
        jPanel.add(jTable, BorderLayout.CENTER);
        jPanel.add(lives, BorderLayout.PAGE_END);

        setTitle("PACMAN");
        setIconImage(new ImageIcon("resources/pacman.png").getImage());

        add(jPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JTable getJTable() {
        return jTable;
    }

    public JLabel getScore() {
        return score;
    }

    public JLabel getTime() {
        return time;
    }

    public JPanel getLives() {
        return lives;
    }

    public JLabel getFirstLife() {
        return firstLife;
    }

    public JLabel getSecondLife() {
        return secondLife;
    }
}
