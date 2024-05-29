package bluepaledot.game;

import java.net.InetAddress;

public class NetPlayer {

    private final InetAddress address;
    private final int port;
    private final String name;
    private int x, y;
    private boolean dying = false;
    private final int id;

    public NetPlayer(String name, InetAddress address, int port, int id) {
        this.address = address;
        this.port = port;
        this.name = name;
        this.y = 430;
        this.id = id;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isDying() {
        return dying;
    }

    public void setDying(boolean dying) {
        this.dying = dying;
    }

    public int getId() {
        return id;
    }

    /**
     * String representation. used for transfer over the network
     */
    public String toString() {
        String retval = "";
        retval += "PLAYER ";
        retval += name + " ";
        retval += x + " ";
        retval += y + " ";
        retval += dying + " ";
        retval += id;
        return retval;
    }
}
