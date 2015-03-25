package minicraftj2dgl.screen;

import minicraftj2dgl.Game;
import minicraftj2dgl.InputHandler;
import minicraftj2dgl.gfx.Screen;

public class LevelTransitionMenu extends Menu {

    private final int dir;
    private int time = 0;

    public LevelTransitionMenu(int dir, Game game, InputHandler inputHandler) {
        super(game, inputHandler);
        this.dir = dir;
    }

    @Override
    public void tick() {
        time += 2;
        if (time == 30) {
            game.changeLevel(dir);
        }
        if (time == 60) {
            game.removeMenu();
        }
    }

    @Override
    public void render(Screen screen) {
        for (int x = 0; x < 40; x++) {
            for (int y = 0; y < 21; y++) {
                int dd = (y + x % 2 * 2 + x / 3) - time;
                if (dd < 0 && dd > -30) {
                    if (dir > 0) {
                        screen.render(x * 8, y * 8, 0, 0, 0);
                    } else {
                        screen.render(x * 8, screen.h - y * 8 - 8, 0, 0, 0);
                    }
                }
            }
        }
    }
}
