package bluepaledot.game;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author angel
 */
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

        // Add the Board panel
        Board gamePanel = new Board();
        gamePanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                gamePanel.requestFocusInWindow();
            }
        });
        cardPanel.add(gamePanel, "game");

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
