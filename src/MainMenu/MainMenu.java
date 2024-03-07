package MainMenu;

import Game.*;
import HighScores.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainMenu extends JFrame {
    public MainMenu() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(3,0, 10, 5));

        ImageIcon newGameIcon = getScaledImageIcon(20, 20, "resources/pacman.png");
        ImageIcon highScoresIcon = getScaledImageIcon(20, 20, "resources/trophy.png");

        JButton newGame = new JButton(newGameIcon);
        newGame.setText("New Game");
        newGame.setVerticalTextPosition(AbstractButton.CENTER);
        newGame.setHorizontalTextPosition(AbstractButton.LEADING);

        JButton highScores = new JButton(highScoresIcon);
        highScores.setText("High Scores");
        highScores.setVerticalTextPosition(AbstractButton.CENTER);
        highScores.setHorizontalTextPosition(AbstractButton.LEADING);

        JButton exit = new JButton("Exit");

        newGame.addActionListener(event -> {
            try {
                int numberOfRows = new SizeInput(this, "Rows").getAmount();
                int numberOfColumns = new SizeInput(this, "Columns").getAmount();
                boolean areNumbersCorrect = (numberOfRows >= 15 && numberOfRows <= 45) && (
                        numberOfColumns >= 15 && numberOfColumns <= 45);
                if (areNumbersCorrect) {
                    dispose();
                    SwingUtilities.invokeLater(() -> new Controller(numberOfRows, numberOfColumns));
                }
            } catch (NullPointerException ignored) {}
        });
        highScores.addActionListener(event -> SwingUtilities.invokeLater(HighScoresWindow::new));
        exit.addActionListener(event -> dispose());

        List<JButton> buttons = new ArrayList<>();
        buttons.add(newGame);
        buttons.add(highScores);
        buttons.add(exit);

        for (JButton button : buttons) {
            jPanel.add(button);
            button.setPreferredSize(new Dimension(jPanel.getWidth(), jPanel.getHeight()));
            button.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
            button.setBackground(Color.BLACK);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
        }

        jPanel.setBackground(Color.BLACK);
        setTitle("PACMAN");
        ImageIcon imageIcon = new ImageIcon("resources/pacman.png");
        setIconImage(imageIcon.getImage());

        add(jPanel);
        setSize(240, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public ImageIcon getScaledImageIcon(int width, int height, String filename) {
        Image image = new ImageIcon(filename).getImage();
        Image newImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }
}
