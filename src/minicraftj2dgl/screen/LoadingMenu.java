package minicraftj2dgl.screen;

import minicraftj2dgl.Game;
import minicraftj2dgl.InputHandler;
import minicraftj2dgl.gfx.Color;
import minicraftj2dgl.gfx.Font;
import minicraftj2dgl.gfx.Screen;

public class LoadingMenu extends Menu {

    public LoadingMenu(Game game, InputHandler input) {
        super(game, input);
    }

    @Override
    public void tick() {
        
    }

    @Override
    public void render(Screen screen) {
        String msg = "   Loading...  "; //15
        int xx = (game.VIEW_WIDTH - msg.length() * 8) / 2;
        int yy = (game.VIEW_HEIGTH - 8) / 2;
        int w = msg.length();
        int h = 1;

        screen.render(xx - 8, yy - 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 0);
        screen.render(xx + w * 8, yy - 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 1);
        screen.render(xx - 8, yy + 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 2);
        screen.render(xx + w * 8, yy + 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 3);
        for (int x = 0; x < w; x++) {
            screen.render(xx + x * 8, yy - 8, 1 + 13 * 32, Color.get(-1, 1, 5, 445), 0);
            screen.render(xx + x * 8, yy + 8, 1 + 13 * 32, Color.get(-1, 1, 5, 445), 2);
        }
        for (int y = 0; y < h; y++) {
            screen.render(xx - 8, yy + y * 8, 2 + 13 * 32, Color.get(-1, 1, 5, 445), 0);
            screen.render(xx + w * 8, yy + y * 8, 2 + 13 * 32, Color.get(-1, 1, 5, 445), 1);
        }
        
        Font.draw(msg, screen, xx, yy, Color.get(5, 333, 333, 333));
    }

}
