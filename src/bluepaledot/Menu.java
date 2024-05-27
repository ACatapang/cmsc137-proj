package bluepaledot;

import bluepaledot.Menu;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class Menu extends JPanel {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    public Menu() {

        cardPanel = new JPanel(cardLayout);

        // Add menu panel
        JPanel menuPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton play_button = new JButton("Play");
        JButton exit_button = new JButton("Exit");
        JButton info_button = new JButton("Info");

        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        menuPanel.add(play_button, gbc);

        gbc.gridy = 1;
        menuPanel.add(exit_button, gbc);

        gbc.gridy = 2;
        menuPanel.add(info_button, gbc);

        cardPanel.add(menuPanel, "menu");

        // Add information panel
        Info infoPanel = new Info();
        cardPanel.add(infoPanel, "info");

        // Add card panel to Menu
        add(cardPanel, BorderLayout.CENTER);

        // Add action listeners to the buttons
        play_button.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(Menu.this, "Play button clicked!");
        });

        exit_button.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });

        info_button.addActionListener((ActionEvent e) -> {
            cardLayout.show(cardPanel, "info");
        });
    }
}
