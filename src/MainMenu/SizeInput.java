package MainMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SizeInput extends JDialog {
    private Integer amount;

    public SizeInput(JFrame jFrame, String input) {
        super(jFrame, true);
        JPanel jPanel = new JPanel();

        JLabel jLabel = new JLabel("Enter a Number of " + input + " (between 15 and 45):");
        jLabel.setForeground(Color.WHITE);

        JTextField jTextField = new JTextField(5);
        jTextField.setBackground(Color.LIGHT_GRAY);
        jTextField.setForeground(Color.BLACK);

        JButton jButton = new JButton("OK");
        jButton.setFocusable(false);
        jButton.setBackground(Color.BLACK);
        jButton.setForeground(Color.WHITE);

        jButton.addActionListener(event -> {
            try {
                amount = Integer.parseInt(jTextField.getText());
                if (amount >= 15 && amount <= 45)
                    dispose();
                else if (amount < 15)
                    JOptionPane.showMessageDialog(this, "Number is too Small",
                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                else
                    JOptionPane.showMessageDialog(this, "Number is too Big",
                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input");
            }
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

        setTitle("Input the Number of " + input);

        add(jPanel);
        pack();
        setLocationRelativeTo(jFrame);
        setVisible(true);
    }

    public Integer getAmount() {
        return amount;
    }
}