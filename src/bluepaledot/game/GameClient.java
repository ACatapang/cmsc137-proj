package bluepaledot.game;

import bluepaledot.game.sprites.Bullet;
import bluepaledot.game.sprites.Enemy;
import bluepaledot.game.sprites.Player;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

public class GameClient extends JPanel implements Runnable, Constants {

    private Dimension d;
    private List<Enemy> enemies;
    private List<Player> players; // List of players
    private Player player;
    private Bullet bullet;

    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private String explImg = "src/images/explosion.png";
    private String message = "Game Over";
    ChatPanel chatPanel;

    private Timer timer;

    Thread t = new Thread(this);
    String name;
    String server = "localhost";
    boolean connected = false;
    DatagramSocket socket = new DatagramSocket();
    String serverData;

    public GameClient(String server, String name) throws Exception {
        this.server = server;
        this.name = name;

        socket.setSoTimeout(100);

        //create the buffer
        // offscreen = (BufferedImage) this.createImage(BOARD_WIDTH, BOARD_HEIGHT);
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

    public void sendChatMessage(String message) {
        send("CHAT " + name + ": " + message);
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

                if (serverData.startsWith("PLAYER")) {
                    // System.out.println(serverData);
                    String[] playersInfo = serverData.split(":");
                    for (int i = 0; i < playersInfo.length; i++) {
                        String[] playerInfo = playersInfo[i].split(" ");
                        String pname = playerInfo[1];
                        int x = Integer.parseInt(playerInfo[2]);
                        int y = Integer.parseInt(playerInfo[3]);
                        // player.setX(x);
                        // player.setY(y);
                        updatePlayerPosition(pname, x, y);

                    }
                    //show the changes
                    repaint();
                }

                if (serverData.startsWith("CHAT")) {
                    // Tokenize chat message
                    String[] tokens = serverData.split(" ", 2);
                    String message = tokens[1];
                    // Display chat message in chat panel
                    chatPanel.addMessage(message);

                    repaint();
                }
            }
        }
    }

    private void updatePlayerPosition(String pname, int x, int y) {
        // Find the player with pname in the list of players
        for (Player player : players) {
            if (player.getName().equals(pname)) {
                // Update the player's position
                player.setX(x);
                player.setY(y);
                return;
            }
        }
        // If player not found, create a new player object and add it to the list
        Player newPlayer = new Player(pname);
        players.add(newPlayer);
    }

    private void initBoard() {
        setLayout(new BorderLayout()); // Use BorderLayout
        chatPanel = new ChatPanel(this, name); // Initialize ChatPanel
        add(chatPanel, BorderLayout.WEST); // Add ChatPanel to the left

        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                doDrawing(g);
            }
        };

        gamePanel.setPreferredSize(new Dimension(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT));
        gamePanel.setBackground(Color.black);
        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(new TAdapter());

        // Add mouse listener to the game panel
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gamePanel.requestFocusInWindow(); // Set focus to the game panel
            }
        });

        add(gamePanel, BorderLayout.EAST);

        d = new Dimension(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);

        timer = new Timer(Constants.DELAY, new GameCycle());
        timer.start();

        gameInit();
    }

    private void gameInit() {

        players = new ArrayList<>();
        enemies = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {

                var enemy = new Enemy(Constants.ENEMY_INIT_X + 30 * j,
                        Constants.ENEMY_INIT_Y + 25 * i);
                enemies.add(enemy);
            }
        }

        player = new Player(name);
        players.add(player);
        bullet = new Bullet();
    }

    private void drawEnemies(Graphics g) {

        for (Enemy enemy : enemies) {

            if (enemy.isVisible()) {

                g.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), this);
            }

            if (enemy.isDying()) {

                enemy.die();
            }
        }
    }

    private void drawPlayer(Graphics g, Player player) {
        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
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

            if (!players.isEmpty()) {
                for (Player player : players) {
                    drawPlayer(g, player);
                }
            }

            drawEnemies(g);
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
        send("PLAYER " + name + " " + player.getX() + " " + player.getY());

        // bullet
        if (bullet.isVisible()) {

            int bulletX = bullet.getX();
            int bulletY = bullet.getY();

            for (Enemy enemy : enemies) {

                int alienX = enemy.getX();
                int alienY = enemy.getY();

                if (enemy.isVisible() && bullet.isVisible()) {
                    if (bulletX >= (alienX)
                            && bulletX <= (alienX + Constants.ENEMY_WIDTH)
                            && bulletY >= (alienY)
                            && bulletY <= (alienY + Constants.ENEMY_HEIGHT)) {

                        var ii = new ImageIcon(explImg);
                        enemy.setImage(ii.getImage());
                        enemy.setDying(true);
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
        for (Enemy enemy : enemies) {

            int x = enemy.getX();

            if (x >= Constants.BOARD_WIDTH - Constants.BORDER_RIGHT && direction != -1) {

                direction = -1;

                for (Enemy a2 : enemies) {
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

            Enemy enemy = it.next();

            if (enemy.isVisible()) {

                int y = enemy.getY();

                if (y > Constants.GROUND - Constants.ENEMY_HEIGHT) {
                    inGame = false;
                    message = "Invasion!";
                }

                enemy.act(direction);
            }
        }

        // bombs
        var generator = new Random();

        for (Enemy enemy : enemies) {

            int shot = generator.nextInt(15);
            Enemy.Bomb bomb = enemy.getBomb();

            if (shot == Constants.CHANCE && enemy.isVisible() && bomb.isDestroyed()) {

                bomb.setDestroyed(false);
                bomb.setX(enemy.getX());
                bomb.setY(enemy.getY());
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
            if (e.getComponent() instanceof JPanel) {
                player.keyReleased(e);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getComponent() instanceof JPanel) {
                player.keyPressed(e);

                int x = player.getX();
                int y = player.getY();

                send("PLAYER " + name + " " + x + " " + y);

                int key = e.getKeyCode();

                if (key == KeyEvent.VK_SPACE) {
                    if (inGame) {
                        if (!bullet.isVisible()&&player.isVisible()) {
                            bullet = new Bullet(x, y);
                        }
                    }

                }
            }

        }
    }
}
