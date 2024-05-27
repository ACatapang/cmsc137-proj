package bluepaledot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Info extends JPanel {

    public Info() {
        setPreferredSize(new Dimension(500, 500));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Set font and color
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.setColor(Color.BLACK);

        String loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

        // Wrap text
        int x = 10;
        int y = 20;
        int maxWidth = getWidth() - 20; // Adjust for padding
        FontMetrics fm = g.getFontMetrics();
        String[] words = loremIpsum.split(" ");
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            if (fm.stringWidth(line + " " + word) < maxWidth) {
                line.append(" ").append(word);
            } else {
                g.drawString(line.toString(), x, y);
                y += fm.getHeight();
                line = new StringBuilder(word);
            }
        }
        g.drawString(line.toString(), x, y);
    }

}
