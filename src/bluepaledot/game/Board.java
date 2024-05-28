package bluepaledot.game;

import bluepaledot.game.sprites.Bullet;
import bluepaledot.game.sprites.Enemy;
import bluepaledot.game.sprites.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements Runnable, Constants {

    private Dimension d;
    private List<Enemy> enemies;
    private Player player;
    private Bullet bullet;

    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private String explImg = "src/images/explosion.png";
    private String message = "Game Over";

    private Timer timer;

    Thread t = new Thread(this);
    String name = "Doe";
    String pname;
    String server = "localhost";
    boolean connected = false;
    DatagramSocket socket = new DatagramSocket();
    String serverData;
    BufferedImage offscreen;

    public Board(String server, String name) throws Exception {
        this.server = server;
        this.name = name;

        socket.setSoTimeout(100);

        //create the buffer
        offscreen = (BufferedImage) this.createImage(BOARD_WIDTH, BOARD_HEIGHT);

        initBoard();

        t.start();
    }

    public void send(String msg) {
        try {
            byte[] buf = msg.getBytes();
            InetAddress address = InetAddress.getByName(server);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
            socket.send(packet);
        } catch (Exception e) {
        }

    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1);
            } catch (Exception ioe) {
            }

            //Get the data from players
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (Exception ioe) {/*lazy exception handling :)*/
            }

            serverData = new String(buf);
            serverData = serverData.trim();

            //if (!serverData.equals("")){
            //	System.out.println("Server Data:" +serverData);
            //}
            //Study the following kids. 
            if (!connected && serverData.startsWith("CONNECTED")) {
                connected = true;
                System.out.println("Connected.");
            } else if (!connected) {
                System.out.println("Connecting..");
                send("CONNECT " + name);
            } else if (connected) {
                if (offscreen != null) {
                    offscreen.getGraphics().clearRect(0, 0, 640, 480);
                    if (serverData.startsWith("PLAYER")) {
                        String[] playersInfo = serverData.split(":");
                        for (int i = 0; i < playersInfo.length; i++) {
                            String[] playerInfo = playersInfo[i].split(" ");
                            String pname = playerInfo[1];
                            int x = Integer.parseInt(playerInfo[2]);
                            int y = Integer.parseInt(playerInfo[3]);
                            //draw on the offscreen image
                            offscreen.getGraphics().fillOval(x, y, 20, 20);
                            offscreen.getGraphics().drawString(pname, x - 10, y + 30);
                        }
                        //show the changes
                        repaint();
                    }
                }

            }
        }
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);
        setBackground(Color.black);

        timer = new Timer(Constants.DELAY, new GameCycle());
        timer.start();

        gameInit();
    }

    private void gameInit() {

        enemies = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {

                var alien = new Enemy(Constants.ENEMY_INIT_X + 30 * j,
                        Constants.ENEMY_INIT_Y + 25 * i);
                enemies.add(alien);
            }
        }

        player = new Player();
        bullet = new Bullet();
    }

    private void drawAliens(Graphics g) {

        for (Enemy alien : enemies) {

            if (alien.isVisible()) {

                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.isDying()) {

                alien.die();
            }
        }
    }

    private void drawPlayer(Graphics g) {

        if (player.isVisible()) {

            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {

            player.die();
            inGame = false;
        }
    }

    private void drawShot(Graphics g) {

        if (bullet.isVisible()) {

            g.drawImage(bullet.getImage(), bullet.getX(), bullet.getY(), this);
        }
    }

    private void drawBombing(Graphics g) {

        for (Enemy a : enemies) {

            Enemy.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        if (inGame) {

            g.drawLine(0, Constants.GROUND,
                    Constants.BOARD_WIDTH, Constants.GROUND);

            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);

        } else {

            if (timer.isRunning()) {
                timer.stop();
            }

            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, Constants.BOARD_HEIGHT / 2 - 25, Constants.BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, Constants.BOARD_HEIGHT / 2 - 25, Constants.BOARD_WIDTH - 100, 50);

        var small = new Font("Helvetica", Font.BOLD, 14);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        int messageWidth = fontMetrics.stringWidth(message);
        int messageHeight = fontMetrics.getAscent();
        g.drawString(message, (Constants.BOARD_WIDTH - messageWidth) / 2, (Constants.BOARD_HEIGHT - messageHeight) / 2);
    }

    private void update() {

        if (deaths == Constants.NUMBER_OF_ENEMIES_TO_DESTROY) {

            inGame = false;
            timer.stop();
            message = "Game won!";
        }

        // player
        player.act();

        // bullet
        if (bullet.isVisible()) {

            int bulletX = bullet.getX();
            int bulletY = bullet.getY();

            for (Enemy alien : enemies) {

                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && bullet.isVisible()) {
                    if (bulletX >= (alienX)
                            && bulletX <= (alienX + Constants.ENEMY_WIDTH)
                            && bulletY >= (alienY)
                            && bulletY <= (alienY + Constants.ENEMY_HEIGHT)) {

                        var ii = new ImageIcon(explImg);
                        alien.setImage(ii.getImage());
                        alien.setDying(true);
                        deaths++;
                        bullet.die();
                    }
                }
            }

            int y = bullet.getY();
            y -= 4;

            if (y < 0) {
                bullet.die();
            } else {
                bullet.setY(y);
            }
        }

        // enemies
        for (Enemy alien : enemies) {

            int x = alien.getX();

            if (x >= Constants.BOARD_WIDTH - Constants.BORDER_RIGHT && direction != -1) {

                direction = -1;

                Iterator<Enemy> i1 = enemies.iterator();

                while (i1.hasNext()) {

                    Enemy a2 = i1.next();
                    a2.setY(a2.getY() + Constants.GO_DOWN);
                }
            }

            if (x <= Constants.BORDER_LEFT && direction != 1) {

                direction = 1;

                Iterator<Enemy> i2 = enemies.iterator();

                while (i2.hasNext()) {

                    Enemy a = i2.next();
                    a.setY(a.getY() + Constants.GO_DOWN);
                }
            }
        }

        Iterator<Enemy> it = enemies.iterator();

        while (it.hasNext()) {

            Enemy alien = it.next();

            if (alien.isVisible()) {

                int y = alien.getY();

                if (y > Constants.GROUND - Constants.ENEMY_HEIGHT) {
                    inGame = false;
                    message = "Invasion!";
                }

                alien.act(direction);
            }
        }

        // bombs
        var generator = new Random();

        for (Enemy alien : enemies) {

            int shot = generator.nextInt(15);
            Enemy.Bomb bomb = alien.getBomb();

            if (shot == Constants.CHANCE && alien.isVisible() && bomb.isDestroyed()) {

                bomb.setDestroyed(false);
                bomb.setX(alien.getX());
                bomb.setY(alien.getY());
            }

            int bombX = bomb.getX();
            int bombY = bomb.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !bomb.isDestroyed()) {

                if (bombX >= (playerX)
                        && bombX <= (playerX + Constants.PLAYER_WIDTH)
                        && bombY >= (playerY)
                        && bombY <= (playerY + Constants.PLAYER_HEIGHT)) {

                    var ii = new ImageIcon(explImg);
                    player.setImage(ii.getImage());
                    player.setDying(true);
                    bomb.setDestroyed(true);
                }
            }

            if (!bomb.isDestroyed()) {

                bomb.setY(bomb.getY() + 1);

                if (bomb.getY() >= Constants.GROUND - Constants.BOMB_HEIGHT) {

                    bomb.setDestroyed(true);
                }
            }
        }
    }

    private void doGameCycle() {

        update();
        repaint();
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {

                if (inGame) {

                    if (!bullet.isVisible()) {

                        bullet = new Bullet(x, y);
                    }
                }
            }
        }
    }
}
