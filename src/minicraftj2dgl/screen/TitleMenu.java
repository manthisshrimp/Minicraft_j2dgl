package minicraftj2dgl.screen;

import javax.swing.JOptionPane;
import minicraftj2dgl.Game;
import minicraftj2dgl.InputHandler;
import minicraftj2dgl.gfx.Color;
import minicraftj2dgl.gfx.Screen;
import minicraftj2dgl.io.GameState;
import minicraftj2dgl.sound.Sound;

public class TitleMenu extends ListMenu {

    public TitleMenu(Game game, InputHandler input) {
        super(new String[]{"Continue", "New Game", "Load Game", "Save Game",
            "How to play", "About", "Quit"}, game, input);
        centerListY = false;
        yOffSet = 42;
    }

    @Override
    public void handleOption(int optionIndex) {
        if (optionIndex == 0) {
            // Continue
            if (!game.loaded) {
                // If no game has been loaded, load quicksave, otherwise close menu.
                Sound.test.play();
                game.saveManager.loadSavedGame("quicksave.sav");
            }
            game.removeMenu();
        } else if (optionIndex == 1) {
            // New Game
            Sound.test.play();
            game.setGameState(GameState.getResetGameState(game, input));
            game.removeMenu();
        } else if (optionIndex == 2) {
            // Load Game
            String[] names = game.saveManager.getSaveGameNames();
            game.menu = new LoadMenu(names, game, input);
        } else if (optionIndex == 3) {
            // Save Game
            String saveName = JOptionPane.showInputDialog("Enter save game name:");
            game.saveManager.saveGame(saveName);
            game.menu = new TitleMenu(game, input);
        } else if (optionIndex == 4) {
            // Instructions
            game.menu = new InstructionsMenu(this, game, input);
        } else if (optionIndex == 5) {
            // About
            game.menu = new AboutMenu(this, game, input);
        } else if (optionIndex == 6) {
            // Quit
            game.exit();
        }
    }

    @Override
    public void render(Screen screen) {
        super.render(screen);
        // Render the title.
        int h = 2;
        int w = 13;
        int titleColor = Color.get(0, 010, 131, 551);
        int titleX = (screen.w - w * 8) / 2;
        int titleY = 12;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                screen.render(titleX + x * 8, titleY + y * 8, x + (y + 6) * 32, titleColor, 0);
            }
        }
    }

    private class LoadMenu extends ListMenu {

        public LoadMenu(String[] options, Game game, InputHandler input) {
            super(options, game, input);
        }

        @Override
        public void handleOption(int optionIndex) {
            game.saveManager.loadSavedGame(options[optionIndex]);
            game.removeMenu();
        }
    }
}
