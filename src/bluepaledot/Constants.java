package bluepaledot;

public interface Constants {
    public static final String APP_NAME = "Blue Pale Dot";

    // UI
    public static final int BOARD_WIDTH = 900;
    public static final int BOARD_HEIGHT = 500;

    // Game states

    public static final int GAME_START = 0;
    public static final int IN_PROGRESS = 1;
    public final int GAME_END = 2;
    public final int WAITING_FOR_PLAYERS = 3;

    public static final int PORT = 4444;
};