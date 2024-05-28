package bluepaledot.game;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class JoinGameMenu extends JPanel {

    private JTextField ipField;
    private JTextField nameField;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    
    public JoinGameMenu(JPanel cardPanel, CardLayout cardLayout) {
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;

        setPreferredSize(new Dimension(300, 200));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Your Name:");
        add(nameLabel, gbc);

        nameField = new JTextField(20);
        gbc.gridy++;
        add(nameField, gbc);

        JLabel ipLabel = new JLabel("Host IP Address:");
        gbc.gridy++;
        add(ipLabel, gbc);

        ipField = new JTextField(20);
        gbc.gridy++;
        add(ipField, gbc);

        JButton joinButton = new JButton("Join");
        gbc.gridy++;
        add(joinButton, gbc);

        joinButton.addActionListener((ActionEvent e) -> {
            String name = nameField.getText();
            String ipAddress = ipField.getText();

            try {
                // Create Board instance
                Board board = new Board(ipAddress, name);

                // Add Board to cardPanel and switch to it
                cardPanel.add(board, "Board");
                cardLayout.show(cardPanel, "Board");
            } catch (Exception ex) {
                ex.printStackTrace(); // Handle exception properly
            }
        });
    }
}
