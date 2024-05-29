package bluepaledot.game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.IntStream;

public class GameServer implements Runnable, Constants {

    String playerData;
    int playerCount = 0;
    DatagramSocket serverSocket = null;
    GameState game;
    int gameStage = WAITING_FOR_PLAYERS;
    int numPlayers;
    Thread t = new Thread(this);
    private TextPanel serverPanel;
    int[] enemyState = {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0 };
    String bombRandomizer = "BOMB" +
            " 0 0 0 0 0 0 0 0" +
            " 0 0 0 0 0 0 0 0" +
            " 0 0 0 0 0 0 0 0" +
            " 0 0 0 0 0 0 0 0" +
            " 0 0 0 0 0 0 0 0";

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
        for (Iterator ite = game.getPlayers().keySet().iterator(); ite.hasNext();) {
            String name = (String) ite.next();
            NetPlayer player = (NetPlayer) game.getPlayers().get(name);
            send(player, msg);
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
        int counter = 0;
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
                        playerCount++;
                        NetPlayer player = new NetPlayer(tokens[1], packet.getAddress(), packet.getPort(),
                                playerCount);
                        game.update(tokens[1].trim(), player);
                        broadcast("CONNECTED " + tokens[1]);
                        String message = "Player connected: " + tokens[1];
                        String lobbyCount = "Players: " + playerCount + "/" + numPlayers;
                        System.out.println(message);
                        serverPanel.log(message);
                        broadcast("CHAT Server: " + message);
                        broadcast("CHAT Server: " + lobbyCount);
                        System.out.println(lobbyCount);
                        if (playerCount == numPlayers) {
                            gameStage = GAME_START;
                        }
                    }
                    break;
                case GAME_START:
                    System.out.println("Game Started");
                    serverPanel.log("Game started");
                    broadcast("START");
                    broadcast("CHAT Server: Game start!!");
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
                    }
                    if (playerData.startsWith("PLAYER")) {
                        // Tokenize:
                        // The format: PLAYER <player name> <x> <y>
                        String[] playerInfo = playerData.split(" ");
                        // System.out.println(playerData);
                        String pname = playerInfo[1];
                        int x = Integer.parseInt(playerInfo[2].trim());
                        int y = Integer.parseInt(playerInfo[3].trim());
                        boolean state = Boolean.parseBoolean(playerInfo[4].trim());
                        // Get the player from the game state
                        NetPlayer player = (NetPlayer) game.getPlayers().get(pname);
                        player.setX(x);
                        player.setY(y);
                        player.setDying(state);
                        // Update the game state
                        game.update(pname, player);
                        // Send to all the updated game state
                        broadcast(game.toString());
                    }
                    if (playerData.startsWith("BULLET")) {
                        // Tokenize:
                        // The format: PLAYER <player name> <x> <y>
                        // System.out.println(playerData);
                        // Update the game state

                        // Send to all the updated game state
                        broadcast(playerData);
                    }
                    // if (playerData.startsWith("ENEMY")) {
                    // // System.out.println(playerData);
                    // String[] enemyInfo = playerData.split(" ", 2);
                    // String[] enemyStateString = enemyInfo[1].split(" ");
                    // int[] playerEnemyState = new int[enemyStateString.length];
                    // for (int i = 0; i < enemyState.length; i++) {
                    // playerEnemyState[i] = Integer.parseInt(enemyStateString[i]);
                    // }
                    // broadcast("ENEMY " + enemyStateSum(playerEnemyState, enemyState).toString());
                    // }
                    if (playerData.startsWith("BOMB")) {
                        // System.out.println(bombRandomizer);
                        broadcast(bombRandomizer);
                    }
                    if (counter == 400) {
                        randomize();
                        counter = 0;
                    } else {
                        counter++;
                    }
                    break;
            }
        }
    }

    // public static int[] enemyStateSum(int[] array1, int[] array2) {
    // return IntStream.range(0, array1.length)
    // .map(i -> array1[i] + array2[i])
    // .toArray();
    // }

    private void randomize() {
        var generator = new Random();
        bombRandomizer = "BOMB";
        for (int i = 0, n = 40; i < n; i++) {
            int x = generator.nextInt(15);
            bombRandomizer += " " + x;
        }
    }
}
