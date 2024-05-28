package bluepaledot.game;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Info extends JPanel {

    private JTextArea textArea;

    public Info(JPanel cardPanel, CardLayout cardLayout) {
        setPreferredSize(new Dimension(500, 400));
        setLayout(null);
        createBackButton(cardPanel, cardLayout);
        createTextArea();
    }

    private void createBackButton(JPanel cardPanel, CardLayout cardLayout) {
        JButton backButton = new JButton("Back");
        // position to center the button at the bottom
        int buttonWidth = 80;
        int buttonHeight = 20;
        int buttonX = (getWidth() - buttonWidth) / 2;
        int buttonY = getHeight() - buttonHeight - 30;
        backButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
        backButton.addActionListener((ActionEvent e) -> {
            cardLayout.show(cardPanel, "menu");
        });
        add(backButton);
    }

    private void createTextArea() {
        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        String loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
        textArea.setText(loremIpsum);

        //position to center the text area
        int textAreaWidth = 400;
        int textAreaHeight = 200;
        int textAreaX = (getWidth() - textAreaWidth) / 2;
        int textAreaY = (getHeight() - textAreaHeight) / 2 - 30;

        textArea.setBounds(textAreaX, textAreaY, textAreaWidth, textAreaHeight);
        add(textArea);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int buttonWidth = 80;
        int buttonHeight = 20;
        int buttonX = (getWidth() - buttonWidth) / 2;
        int buttonY = getHeight() - buttonHeight - 30;
        getComponent(0).setBounds(buttonX, buttonY, buttonWidth, buttonHeight);

        int textAreaWidth = 400;
        int textAreaHeight = 200;
        int textAreaX = (getWidth() - textAreaWidth) / 2;
        int textAreaY = (getHeight() - textAreaHeight) / 2 - 30;
        getComponent(1).setBounds(textAreaX, textAreaY, textAreaWidth, textAreaHeight);
    }
}
