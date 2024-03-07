package HighScores;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class HighScoresWindow extends JFrame {
    private HighScores highScores;

    public HighScoresWindow() {
        JList<String> jList = new JList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream("HighScores.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            highScores = (HighScores) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException ignore) {}

        HighScoresModel highScoresModel;
        if (highScores != null)
            highScoresModel = new HighScoresModel(highScores.getHighScores());
        else
            highScoresModel = new HighScoresModel(new HashMap<>());
        HighScoresRenderer highScoresRenderer = new HighScoresRenderer();
        jList.setModel(highScoresModel);
        jList.setCellRenderer(highScoresRenderer);
        jList.setBackground(Color.BLACK);

        JScrollPane jScrollPane = new JScrollPane(jList);

        ImageIcon imageIcon = new ImageIcon("resources/trophy.png");
        setIconImage(imageIcon.getImage());

        add(jScrollPane);
        setSize(200, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
