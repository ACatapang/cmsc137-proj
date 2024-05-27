package bluepaledot.game.sprites;

import bluepaledot.game.Constants;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Player extends Sprite {

    private int width;

    public Player() {
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

    public void act() {
        // System.out.println("Player dx value: " + dx);
        // System.out.println("Player x position before act: " + x);
        x += dx;

        if (x <= 2) {
            x = 2;
        }

        if (x >= Constants.BOARD_WIDTH - 2 * width) {
            x = Constants.BOARD_WIDTH - 2 * width;
        }
        // System.out.println("Player x position after act: " + x);
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -2;
            // System.out.println("Left key pressed, dx set to " + dx);
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 2;
            // System.out.println("Right key pressed, dx set to " + dx);
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            dx = 0;
            // System.out.println("Key released, dx set to " + dx);
        }
    }
}
