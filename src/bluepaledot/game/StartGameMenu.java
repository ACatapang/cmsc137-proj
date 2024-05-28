package bluepaledot.game;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class StartGameMenu extends JPanel {

    private JTextField ipField;
    private JTextField playerCountField;

    public StartGameMenu(JPanel cardPanel, CardLayout cardLayout) {
        setPreferredSize(new Dimension(300, 200));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel ipLabel = new JLabel("Host IP Address:");
        add(ipLabel, gbc);

        ipField = new JTextField(20);
        gbc.gridy++;
        add(ipField, gbc);

        JLabel playerCountLabel = new JLabel("Number of Players:");
        gbc.gridy++;
        add(playerCountLabel, gbc);

        playerCountField = new JTextField(5);
        gbc.gridy++;
        add(playerCountField, gbc);

        JButton startButton = new JButton("Start");
        gbc.gridy++;
        add(startButton, gbc);

        startButton.addActionListener((ActionEvent e) -> {
            String ipAddress = ipField.getText();
            int playerCount = Integer.parseInt(playerCountField.getText());

            // Create the ServerPanel and pass it to GameServer
            TextPanel serverPanel = new TextPanel();
            cardPanel.add(serverPanel, "ServerPanel");
            cardLayout.show(cardPanel, "ServerPanel");

            new GameServer(playerCount, serverPanel);
        });
    }
}
