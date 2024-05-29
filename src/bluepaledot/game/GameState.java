package bluepaledot.game;

import java.util.HashMap;
import java.util.Map;

import bluepaledot.game.sprites.Bullet;

public class GameState {
    private Map<String, NetPlayer> players;
    private Map<String, Bullet> bullets;

    public GameState() {
        players = new HashMap<>();
        bullets = new HashMap<>();
    }

    public Map<String, NetPlayer> getPlayers() {
        return players;
    }

    public Map<String, Bullet> getBullets() {
        return bullets;
    }

    public void updatePlayer(String name, NetPlayer player) {
        players.put(name, player);
    }

    public void updateBullet(String playerName, Bullet bullet) {
        bullets.put(playerName, bullet);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (NetPlayer player : players.values()) {
            sb.append("PLAYER ").append(player.getName()).append(" ")
              .append(player.getX()).append(" ").append(player.getY()).append(":");
        }

        for (Map.Entry<String, Bullet> entry : bullets.entrySet()) {
            Bullet bullet = entry.getValue();
            sb.append("BULLET ").append(entry.getKey()).append(" ")
              .append(bullet.getX()).append(" ").append(bullet.getY()).append(":");
        }

        return sb.toString();
    }
}
