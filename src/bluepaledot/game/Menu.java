package bluepaledot.game;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class Menu extends JPanel {

    public Menu(JPanel cardPanel, CardLayout cardLayout) {
        setPreferredSize(new Dimension(300, 200));
        setLayout(new GridBagLayout());

        JButton startButton = new JButton("Start Game");
        JButton joinButton = new JButton("Join Game");
        JButton exitButton = new JButton("Exit");
        JButton infoButton = new JButton("Info");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        add(startButton, gbc);
        gbc.gridy++;
        add(joinButton, gbc);
        gbc.gridy++;
        add(exitButton, gbc);
        gbc.gridy++;
        add(infoButton, gbc);

        startButton.addActionListener((ActionEvent e) -> {
            cardLayout.show(cardPanel, "startGame");
        });

        joinButton.addActionListener((ActionEvent e) -> {
            cardLayout.show(cardPanel, "joinGame");
        });

        exitButton.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });

        infoButton.addActionListener((ActionEvent e) -> {
            cardLayout.show(cardPanel, "info");
        });
    }
}
