package bluepaledot.game;

import java.awt.*;
import javax.swing.*;

public class TextPanel extends JPanel {

    private JLabel logLabel;

    public TextPanel() {
        setLayout(new GridBagLayout());

        // Centered log message
        logLabel = new JLabel();
        logLabel.setFont(new Font("Arial", Font.PLAIN, 24)); // Set font size to 24 pt
        logLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // GridBagConstraints to center the label
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;

        add(logLabel, gbc);

        setPreferredSize(new Dimension(400, 300));
    }

    public void log(String message) {
        logLabel.setText("<html><div style='text-align: center;'>" + message + "</div></html>");
    }
}
