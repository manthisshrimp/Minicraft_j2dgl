package minicraftj2dgl.io;

import minicraftj2dgl.entity.mob.Player;
import java.io.Serializable;
import minicraftj2dgl.level.Level;
import minicraftj2dgl.Game;
import minicraftj2dgl.InputHandler;

public class GameState implements Serializable {

    public int playerDeadTime;
    public int wonTimer;
    public int gameTime;
    public boolean hasWon;
    public Level[] levels;
    public Level level;
    public int currentLevel;
    public Player player;

    public static GameState getResetGameState(Game game, InputHandler input) {
        GameState gameState = new GameState();
        gameState.playerDeadTime = 0;
        gameState.wonTimer = 0;
        gameState.gameTime = 0;
        gameState.hasWon = false;

        gameState.levels = new Level[5];

        gameState.levels[4] = new Level(128, 128, 1, null);
        gameState.levels[3] = new Level(128, 128, 0, gameState.levels[4]);
        gameState.levels[2] = new Level(128, 128, -1, gameState.levels[3]);
        gameState.levels[1] = new Level(128, 128, -2, gameState.levels[2]);
        gameState.levels[0] = new Level(128, 128, -3, gameState.levels[1]);
        
        gameState.currentLevel = 3;
        gameState.level = gameState.levels[gameState.currentLevel];

        gameState.player = new Player(game, input);
        gameState.player.findStartPos(gameState.level);

        gameState.level.add(gameState.player);

        for (int i = 0; i < 5; i++) {
            gameState.levels[i].trySpawn(5000);
        }
        
        return gameState;
    }
}
