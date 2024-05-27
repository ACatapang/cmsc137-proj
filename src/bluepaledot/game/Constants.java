package bluepaledot.game;

public interface Constants {

    public static final String APP_NAME = "Blue Pale Dot";

    // UI
    public static final int BOARD_WIDTH = 900;
    public static final int BOARD_HEIGHT = 500;
    int BORDER_RIGHT = 30;
    int BORDER_LEFT = 5;

    int GROUND = 450;
    int BOMB_HEIGHT = 5;

    int ENEMY_HEIGHT = 12;
    int ENEMY_WIDTH = 12;
    int ENEMY_INIT_X = 150;
    int ENEMY_INIT_Y = 5;

    int GO_DOWN = 15;
    int NUMBER_OF_ENEMIES_TO_DESTROY = 40;
    int CHANCE = 5;
    int DELAY = 17;
    int PLAYER_WIDTH = 15;
    int PLAYER_HEIGHT = 10;

    // Game states
    public static final int GAME_START = 0;
    public static final int IN_PROGRESS = 1;
    public final int GAME_END = 2;
    public final int WAITING_FOR_PLAYERS = 3;

    public static final int PORT = 4444;
};
