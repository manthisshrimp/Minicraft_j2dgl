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
            if (game.loaded) {
                game.removeMenu();
            } else {
                // If no game has been loaded, load last save.
                String lastSaveName = game.config.getEntry("lastSave", String.class);
                GameState gameState = game.saveManager.loadSavedGame(lastSaveName);
                if (gameState != null) {
                    game.setGameState(gameState);
                    game.removeMenu();
                    Sound.test.play();
                } else {
                    // TODO: Handle "Continue" option when no quicksave exists.
                    JOptionPane.showMessageDialog(null, "Error loading \"" + lastSaveName + "\"");
                }
            }
        } else if (optionIndex == 1) {
            // New Game
            Sound.test.play();
            game.setGameState(GameState.getResetGameState(game, input));
            game.removeMenu();
        } else if (optionIndex == 2) {
            // Load Game
            String[] names = game.saveManager.getSaveGameNames();
            if (names.length > 0) {
                game.menu = new LoadMenu(names, game, input);
            }
        } else if (optionIndex == 3) {
            // Save Game
            String saveName = JOptionPane.showInputDialog("Enter save game name:");
            boolean successful = game.saveManager.saveGame(saveName);
            if (!successful) {
                JOptionPane.showMessageDialog(null, "Error saving \"" + saveName + ".sav\"");
            }
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
            GameState gameState = game.saveManager.loadSavedGame(options[optionIndex]);
            if (gameState == null) {
                JOptionPane.showMessageDialog(null, "Error loading \"" + options[optionIndex] + "\"");
            } else {
                game.setGameState(gameState);
                game.removeMenu();
            }
        }
    }
}
