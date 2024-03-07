package HighScores;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class NameInput extends JDialog {
    private String username;

    public NameInput(JFrame jFrame) {
        super(jFrame, true);
        JPanel jPanel = new JPanel();

        JLabel jLabel = new JLabel("Enter your Name:");
        jLabel.setForeground(Color.WHITE);

        JTextField jTextField = new JTextField(10);
        jTextField.setBackground(Color.LIGHT_GRAY);
        jTextField.setForeground(Color.BLACK);

        JButton jButton = new JButton("OK");
        jButton.setFocusable(false);
        jButton.setBackground(Color.BLACK);
        jButton.setForeground(Color.WHITE);

        jButton.addActionListener(event -> {
            username = jTextField.getText();
            if (!username.equals(""))
                dispose();
            else JOptionPane.showMessageDialog(this, "Your name must contain Characters!",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        });

        jTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    jButton.doClick();
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });

        jPanel.add(jLabel);
        jPanel.add(jTextField);
        jPanel.add(jButton);

        jPanel.setBackground(Color.BLACK);

        setTitle("Game Over!");

        add(jPanel);
        pack();
        setLocationRelativeTo(jFrame);
        setVisible(true);
    }

    public String getUsername() {
        return username;
    }
}
