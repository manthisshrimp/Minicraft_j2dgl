package minicraftj2dgl.io;

import minicraftj2dgl.entity.mob.Player;
import java.io.Serializable;
import minicraftj2dgl.level.Level;

public class GameState implements Serializable {

    public int playerDeadTime;
    public int wonTimer;
    public int gameTime;
    public boolean hasWon;
    public Level[] levels;
    public int currentLevel;
    public Player player;
}
