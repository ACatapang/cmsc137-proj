package bluepaledot;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Menu extends JPanel {

    private JPanel cardPanel;
    private CardLayout cardLayout;

    public Menu(JPanel cardPanel, CardLayout cardLayout) {
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;

        setPreferredSize(new Dimension(300, 200));
        setLayout(new GridBagLayout());

        JButton play_button = new JButton("Play");
        JButton exit_button = new JButton("Exit");
        JButton info_button = new JButton("Info");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Add Play button
        add(play_button, gbc);

        // Add Exit button
        gbc.gridy++;
        add(exit_button, gbc);

        // Add Info button
        gbc.gridy++;
        add(info_button, gbc);

        // Add action listeners to the buttons
        play_button.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(Menu.this, "Play button clicked!");
            // Handle play button click
        });

        exit_button.addActionListener((ActionEvent e) -> {
            System.exit(0);

        });

        info_button.addActionListener((ActionEvent e) -> {
            cardLayout.show(cardPanel, "info");

        });
    }
}
