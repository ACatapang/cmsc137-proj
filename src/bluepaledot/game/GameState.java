package bluepaledot.game;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * The class that contains the state of the game. The game state refers the
 * current position of the players etc.
 *
 * @author Joseph Anthony C. Hermocilla
 *
 */
public class GameState {

    /**
     * This is a map(key-value pair) of <player name,NetPlayer>
     */
    private Map<String, NetPlayer> players = new HashMap<>();
    

    /**
     * Simple constructor
     *
     */
    public GameState() {
    }

    /**
     * Update the game state. Called when player moves
     *
     * @param name
     * @param player
     */
    public void updatePlayer(String name, NetPlayer player) {
        players.put(name, player);
    }
    
    /**
     * String representation of this object. Used for data transfer over the
     * network
     */
    public String toString() {
        StringBuilder retval = new StringBuilder();
        // Append players' string representations
        for (String name : players.keySet()) {
            NetPlayer player = players.get(name);
            retval.append(player.toString()).append(":");
        }
        return retval.toString();
    }
    /**
     * Returns the map
     *
     * @return
     */
    public Map<String, NetPlayer> getPlayers() {
        return players;
    }
}