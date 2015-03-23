package minicraftj2dgl.screen;

import minicraftj2dgl.Game;
import minicraftj2dgl.InputHandler;
import minicraftj2dgl.gfx.Color;
import minicraftj2dgl.gfx.Font;
import minicraftj2dgl.gfx.Screen;

public abstract class ListMenu extends Menu {

    protected final String[] options;
    private int selected = 0;
    private String prompt = "(Arrow keys,X and C)";

    protected int xOffSet = 0, yOffSet = 0;
    protected boolean centerListX = true;
    protected boolean centerListY = true;

    public ListMenu(String[] options, Game game, InputHandler input) {
        super(game, input);
        this.options = options;
    }

    public ListMenu(String prompt, String[] options, Game game, InputHandler input) {
        this(options, game, input);
        this.prompt = prompt;
    }

    public void setCenterListX(boolean centerListX) {
        this.centerListX = centerListX;
    }

    public void setCenterListY(boolean centerListY) {
        this.centerListY = centerListY;
    }

    @Override
    public void tick() {
        if (input.up.clicked) {
            selected--;
        }
        if (input.down.clicked) {
            selected++;
        }
        int len = options.length;
        if (selected < 0) {
            selected += len;
        }
        if (selected >= len) {
            selected -= len;
        }
        if (input.attack.clicked || input.menu.clicked) {
            handleOption(selected);
        }
    }

    public abstract void handleOption(int optionIndex);

    @Override
    public void render(Screen screen) {
        screen.clear(0);
        if (centerListY) {
            yOffSet = (screen.h - options.length * 8) / 2;
        }
        for (int i = 0; i < options.length; i++) {
            String option = options[i];
            int col = Color.get(0, 222, 222, 222);
            if (i == selected) {
                option = "> " + option + " <";
                col = Color.get(0, 555, 555, 555);
            }
            if (centerListX) {
                xOffSet = (screen.w - option.length() * 8) / 2;
            }
            Font.draw(option, screen, xOffSet, yOffSet + i * 8, col);
        }
        Font.draw(prompt, screen, 0, screen.h - 8, Color.get(0, 111, 111, 111));
    }
}
