package bluepaledot.game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Iterator;

import bluepaledot.game.sprites.Bullet;

public class GameServer implements Runnable, Constants {

    String playerData;
    int playerCount = 0;
    DatagramSocket serverSocket = null;
    GameState game;
    int gameStage = WAITING_FOR_PLAYERS;
    int numPlayers;
    Thread t = new Thread(this);
    private TextPanel serverPanel;

    public GameServer(int numPlayers, TextPanel serverPanel) {
        this.numPlayers = numPlayers;
        this.serverPanel = serverPanel;
        try {
            serverSocket = new DatagramSocket(PORT);
            serverSocket.setSoTimeout(100);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + PORT);
            System.exit(-1);
        } catch (Exception e) {
        }
        // Create the game state
        game = new GameState();

        System.out.println("Game created...");
        serverPanel.log("Game Server started, waiting for players...");
        System.out.println("Waiting for Players...");

        // Start the game thread
        t.start();
    }

    public void broadcast(String msg) {
        for (Iterator<String> ite = game.getPlayers().keySet().iterator(); ite.hasNext();) {
            String name = ite.next();
            NetPlayer player = game.getPlayers().get(name);
            send(player, msg);
        }
    }

    public void broadcastPlayerPositions() {
        for (NetPlayer player : game.getPlayers().values()) {
            // Construct the message containing player position
            String playerPositionMsg = "PLAYER " + player.getName() + " " + player.getX() + " " + player.getY();
            // Broadcast the player position message to all clients
            broadcast(playerPositionMsg);
        }
    }

    public void broadcastGameState() {
        String gameState = game.toString();
        
        for (Object playerObj : game.getPlayers().values()) {
            if (playerObj instanceof NetPlayer) {
                NetPlayer player = (NetPlayer) playerObj;
                send(player, "GAMESTATE " + gameState);
            } else {
                // Handle unexpected type
                System.err.println("Unexpected type encountered: " + playerObj.getClass());
            }
        }
    }

    public void send(NetPlayer player, String msg) {
        DatagramPacket packet;
        byte buf[] = msg.getBytes();
        packet = new DatagramPacket(buf, buf.length, player.getAddress(), player.getPort());
        try {
            serverSocket.send(packet);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                serverSocket.receive(packet);
            } catch (Exception ioe) {
            }

            playerData = new String(buf).trim();

            switch (gameStage) {
                case WAITING_FOR_PLAYERS:
                    if (playerData.startsWith("CONNECT")) {
                        String tokens[] = playerData.split(" ");
                        NetPlayer player = new NetPlayer(tokens[1], packet.getAddress(), packet.getPort());
                        System.out.println("Player connected: " + tokens[1]);
                        serverPanel.log("Player connected: " + tokens[1]);
                        game.updatePlayer(tokens[1].trim(), player);
                        broadcast("CONNECTED " + tokens[1]);
                        playerCount++;
                        if (playerCount == numPlayers) {
                            gameStage = GAME_START;
                        }
                    }
                    break;
                case GAME_START:
                    System.out.println("Game Started");
                    serverPanel.log("Game started");
                    broadcast("START");
                    gameStage = IN_PROGRESS;
                    break;
                case IN_PROGRESS:
                    // Player data was received!
                    if (playerData.startsWith("CHAT")) {
                        // Tokenize chat message
                        String[] tokens = playerData.split(" ", 2);
                        String message = tokens[1];
                        // Broadcast chat message to all players
                        broadcast("CHAT " + message);
                    } else if (playerData.startsWith("PLAYER")) {
                        // Tokenize:
                        // The format: PLAYER <player name> <x> <y>
                        String[] playerInfo = playerData.split(" ");
                        String pname = playerInfo[1];
                        int x = Integer.parseInt(playerInfo[2].trim());
                        int y = 430;
                        // Get the player from the game state
                        NetPlayer player = (NetPlayer) game.getPlayers().get(pname);
                        player.setX(x);
                        player.setY(y);
                        // Update the game state
                        game.updatePlayer(pname, player);
                        // Send to all the updated game state
                        broadcast(game.toString());
                    } else if (playerData.startsWith("BULLET")) {
                        String[] bulletInfo = playerData.split(" ");
                        String pname = bulletInfo[1];
                        int x = Integer.parseInt(bulletInfo[2].trim());
                        int y = Integer.parseInt(bulletInfo[3].trim());
                        Bullet bullet = new Bullet(x, y);
                        game.updateBullet(pname, bullet);
                        broadcast(game.toString());
                    }
                    break;
            }
        }
    }
}
