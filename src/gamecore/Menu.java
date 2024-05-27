package gamecore;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Menu extends JPanel {

    private JPanel currentPanel; // Track the current panel being displayed

    public Menu() {
        setLayout(new GridBagLayout());

        JButton play_button = new JButton("Play");
        JButton exit_button = new JButton("Exit");
        JButton info_button = new JButton("Info");
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Add Play button
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(play_button, gbc);

        // Add Exit button
        gbc.gridy = 1;
        add(exit_button, gbc);

        // Add Info button
        gbc.gridy = 2;
        add(info_button, gbc);

        // Add action listeners to the buttons

        play_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Menu.this, "Play button clicked!");
                // Handle play button click
            }
        });

        exit_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        info_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display the Info panel
                
            }
        });
    }


}
