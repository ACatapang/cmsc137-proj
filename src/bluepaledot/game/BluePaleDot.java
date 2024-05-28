package bluepaledot.game;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BluePaleDot extends JFrame {

    private JPanel cardPanel;
    private CardLayout cardLayout;

    public BluePaleDot() {
        initUI();
    }

    private void initUI() {
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        Menu menuPanel = new Menu(cardPanel, cardLayout);
        cardPanel.add(menuPanel, "menu");

        StartGameMenu startGamePanel = new StartGameMenu(cardPanel, cardLayout);
        cardPanel.add(startGamePanel, "startGame");

        JoinGameMenu joinGamePanel = new JoinGameMenu(cardPanel, cardLayout);
        cardPanel.add(joinGamePanel, "joinGame");

        Info infoPanel = new Info(cardPanel, cardLayout);
        cardPanel.add(infoPanel, "info");

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);

        setTitle("Blue Pale Dot");
        setSize(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var ex = new BluePaleDot();
            ex.setVisible(true);
        });
    }
}
