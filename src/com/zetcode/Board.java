package com.zetcode;

import com.zetcode.sprite.Alien;
import com.zetcode.sprite.Player;
import com.zetcode.sprite.Shot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Board extends JPanel {

    private Dimension d;
    private List<Alien> aliens;
    private Player player;
    private Shot shot;

    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private String explImg = "src/images/explosion.png";
    private String message = "Game Over";

    private Timer timer;
    private int gameState;

    private String chatMessage;
    private int messageTimer;

    private JTextField textField;
    private boolean typingMessage;

    public Board() {
        initBoard();
        initMessage();
        initTextInput();
    }

    private void initTextInput() {
        textField = new JTextField();
        textField.setBounds(10, 10, 200, 20);
        textField.setVisible(false); // Initially hide the text input field
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String message = textField.getText();
                    setChatMessage(message);
                    textField.setText("");
                    typingMessage = false; // Stop typing after Enter is pressed
                    textField.setVisible(false); // Hide the text input field
                    requestFocusInWindow(); // Request focus back to the game for keyboard input
                }
            }
        });
        add(textField);
    }

    private void initMessage() {
        chatMessage = "";
        typingMessage = false;
    }

    public void setChatMessage(String message) {
        this.chatMessage = message;
        repaint();
    }

    private void drawMessage(Graphics g) {
        if (!chatMessage.isEmpty()) {
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.PLAIN, 12)); // Use a smaller font
            FontMetrics fontMetrics = g.getFontMetrics();
            int x = Commons.BOARD_WIDTH - fontMetrics.stringWidth(chatMessage) - 10; // Align to the top right
            int y = 20; // Set y position at the top
            g.drawString(chatMessage, x, y);
        }
    }

    private void initBoard() {
        setLayout(null);  // Use null layout for absolute positioning

        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
        setBackground(Color.black);

        gameState = Commons.MENU_STATE;

        timer = new Timer(Commons.DELAY, new GameCycle());
        timer.start();

        initMenu();
    }

    private void initMenu() {
        JButton startButton = new JButton("Start Game");
        startButton.setBounds(120, 150, 120, 30);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameState = Commons.GAME_STATE;
                gameInit();
                remove(startButton);
                repaint();
                requestFocusInWindow();  // Request focus to capture keyboard input
            }
        });

        this.add(startButton);
    }

    private void initGameOver() {
        JButton retryButton = new JButton("Retry");
        retryButton.setBounds(120, 150, 120, 30);
        retryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameState = Commons.GAME_STATE;
                gameInit();
                remove(retryButton);
                repaint();
                requestFocusInWindow();  // Request focus to capture keyboard input
            }
        });

        this.add(retryButton);
    }

    private void gameInit() {
        aliens = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                var alien = new Alien(Commons.ALIEN_INIT_X + 18 * j, Commons.ALIEN_INIT_Y + 18 * i);
                aliens.add(alien);
            }
        }

        player = new Player();
        shot = new Shot();

        inGame = true;
        direction = -1;
        deaths = 0;

        if (!timer.isRunning()) {
            timer.restart();
        }
    }

    private void drawAliens(Graphics g) {
        for (Alien alien : aliens) {
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
            gameState = Commons.GAME_OVER_STATE;
            initGameOver();
        }
    }

    private void drawShot(Graphics g) {
        if (shot.isVisible()) {
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }

    private void drawBombing(Graphics g) {
        for (Alien a : aliens) {
            Alien.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }

    // Existing code...

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
        drawMessage(g); // Add this line to draw the chat message
    }

    private void doDrawing(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        if (gameState == Commons.GAME_STATE) {
            g.drawLine(0, Commons.GROUND, Commons.BOARD_WIDTH, Commons.GROUND);
            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);

            if (!inGame) {
                if (timer.isRunning()) {
                    timer.stop();
                }
                gameOver(g);
            }
        } else if (gameState == Commons.GAME_OVER_STATE) {
            gameOver(g);
        } else if (gameState == Commons.PAUSE_STATE) {
            drawPause(g);
        } else {
            drawMenu(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawMenu(Graphics g) {
        g.setColor(Color.white);
        var large = new Font("Helvetica", Font.BOLD, 18);
        var fontMetrics = this.getFontMetrics(large);
        g.setFont(large);
        g.drawString("Space Invaders", (Commons.BOARD_WIDTH - fontMetrics.stringWidth("Space Invaders")) / 2, Commons.BOARD_HEIGHT / 2 - 50);
    }

    private void drawPause(Graphics g) {
        g.setColor(Color.white);
        var large = new Font("Helvetica", Font.BOLD, 18);
        var fontMetrics = this.getFontMetrics(large);
        g.setFont(large);
        g.drawString("Paused", (Commons.BOARD_WIDTH - fontMetrics.stringWidth("Paused")) / 2, Commons.BOARD_HEIGHT / 2);
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);

        var small = new Font("Helvetica", Font.BOLD, 14);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (Commons.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2, Commons.BOARD_WIDTH / 2);
    }

    private void update() {
        if (deaths == Commons.NUMBER_OF_ALIENS_TO_DESTROY) {
            inGame = false;
            timer.stop();
            message = "Game won!";
            gameState = Commons.GAME_OVER_STATE;
            initGameOver();
        }

        // player
        player.act();

        // shot
        if (shot.isVisible()) {
            int shotX = shot.getX();
            int shotY = shot.getY();

            for (Alien alien : aliens) {
                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && shot.isVisible()) {
                    if (shotX >= (alienX) && shotX <= (alienX + Commons.ALIEN_WIDTH) && shotY >= (alienY) && shotY <= (alienY + Commons.ALIEN_HEIGHT)) {
                        var ii = new ImageIcon(explImg);
                        alien.setImage(ii.getImage());
                        alien.setDying(true);
                        deaths++;
                        shot.die();
                    }
                }
            }

            int y = shot.getY();
            y -= 4;

            if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }

        // aliens

        for (Alien alien : aliens) {
            int x = alien.getX();

            if (x >= Commons.BOARD_WIDTH - Commons.BORDER_RIGHT && direction != -1) {
                direction = -1;

                Iterator<Alien> i1 = aliens.iterator();

                while (i1.hasNext()) {
                    Alien a2 = i1.next();
                    a2.setY(a2.getY() + Commons.GO_DOWN);
                }
            }

            if (x <= Commons.BORDER_LEFT && direction != 1) {
                direction = 1;

                Iterator<Alien> i2 = aliens.iterator();

                while (i2.hasNext()) {
                    Alien a = i2.next();
                    a.setY(a.getY() + Commons.GO_DOWN);
                }
            }
        }

        Iterator<Alien> it = aliens.iterator();

        while (it.hasNext()) {
            Alien alien = it.next();

            if (alien.isVisible()) {
                int y = alien.getY();

                if (y > Commons.GROUND - Commons.ALIEN_HEIGHT) {
                    inGame = false;
                    message = "Invasion!";
                    gameState = Commons.GAME_OVER_STATE;
                    initGameOver();
                }

                alien.act(direction);
            }
        }

        // bombs
        var generator = new Random();

        for (Alien alien : aliens) {
            int shot = generator.nextInt(15);
            Alien.Bomb bomb = alien.getBomb();

            if (shot == Commons.CHANCE && alien.isVisible() && bomb.isDestroyed()) {
                bomb.setDestroyed(false);
                bomb.setX(alien.getX());
                bomb.setY(alien.getY());
            }

            int bombX = bomb.getX();
            int bombY = bomb.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !bomb.isDestroyed()) {
                if (bombX >= (playerX) && bombX <= (playerX + Commons.PLAYER_WIDTH) && bombY >= (playerY) && bombY <= (playerY + Commons.PLAYER_HEIGHT)) {
                    var ii = new ImageIcon(explImg);
                    player.setImage(ii.getImage());
                    player.setDying(true);
                    bomb.setDestroyed(true);
                }
            }

            if (!bomb.isDestroyed()) {
                bomb.setY(bomb.getY() + 1);

                if (bomb.getY() >= Commons.GROUND - Commons.BOMB_HEIGHT) {
                    bomb.setDestroyed(true);
                }
            }
        }
    }

    private void doGameCycle() {
        if (gameState == Commons.GAME_STATE) {
            update();
        }
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
    
            int key = e.getKeyCode();
    
            switch (gameState) {
                case Commons.GAME_STATE:
                    handleGameInput(key);
                    break;
                case Commons.PAUSE_STATE:
                    handlePauseInput(key);
                    break;
                case Commons.MENU_STATE:
                    handleMenuInput(key);
                    break;
                default:
                    break;
            }
        }
    
        private void handleGameInput(int key) {
            if (key == KeyEvent.VK_SPACE && inGame) {
                shoot();
            } else if (key == KeyEvent.VK_P) {
                gameState = Commons.PAUSE_STATE;
            } else if (key == KeyEvent.VK_ENTER && !typingMessage) {
                startTypingMessage();
            }
        }
    
        private void handlePauseInput(int key) {
            if (key == KeyEvent.VK_P) {
                gameState = Commons.GAME_STATE;
            } else {
                // If any other key is pressed, handle it normally
                handleGameInput(key);
            }
        }
    
        private void handleMenuInput(int key) {
            // Handle menu input here
        }
    
        private void shoot() {
            int x = player.getX();
            int y = player.getY();
            if (!shot.isVisible()) {
                shot = new Shot(x, y);
            }
        }
    
        private void startTypingMessage() {
            typingMessage = true;
            textField.setVisible(true);
            textField.requestFocus();
        }
    }
    


    
}
