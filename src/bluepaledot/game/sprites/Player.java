package bluepaledot.game.sprites;

import bluepaledot.game.Constants;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Player extends Sprite {

    private String name; // Add a field for the player's name
    private int width;

    // Update the constructor to accept a name parameter
    public Player(String name) {
        this.name = name;
        initPlayer();
    }

    private void initPlayer() {
        var playerImg = "src/images/ship1.png";
        var ii = new ImageIcon(playerImg);

        width = ii.getImage().getWidth(null);
        setImage(ii.getImage());

        int START_X = 450;
        setX(START_X);

        int START_Y = 430;
        setY(START_Y);
    }

    // Add a getter method for the player's name
    public String getName() {
        return name;
    }

    public void act() {
        x += dx;

        if (x <= 2) {
            x = 2;
        }

        if (x >= Constants.BOARD_WIDTH - 2 * width) {
            x = Constants.BOARD_WIDTH - 2 * width;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -2;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 2;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
    }
}
