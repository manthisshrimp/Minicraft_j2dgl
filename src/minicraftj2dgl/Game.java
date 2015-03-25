package minicraftj2dgl;

import j2dgl.Core;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import minicraftj2dgl.entity.mob.Player;
import minicraftj2dgl.gfx.Color;
import minicraftj2dgl.gfx.Font;
import minicraftj2dgl.gfx.Screen;
import minicraftj2dgl.gfx.SpriteSheet;
import minicraftj2dgl.io.GameConfig;
import minicraftj2dgl.io.GameState;
import minicraftj2dgl.io.SaveManager;
import minicraftj2dgl.level.Level;
import minicraftj2dgl.level.tile.Tile;
import minicraftj2dgl.screen.DeadMenu;
import minicraftj2dgl.screen.LevelTransitionMenu;
import minicraftj2dgl.screen.Menu;
import minicraftj2dgl.screen.TitleMenu;
import minicraftj2dgl.screen.WonMenu;

public class Game extends Core {

    // Core
    public Player player;
    public Menu menu;

    private Level[] levels = new Level[5];
    private int currentLevel = 3;
    private Level level = levels[currentLevel];

    // Rendering
    public final int VIEW_WIDTH = 640 / 2;
    public final int VIEW_HEIGTH = 360 / 2;
    private Screen screen;
    private Screen lightScreen;
    private final BufferedImage image = new BufferedImage(VIEW_WIDTH, VIEW_HEIGTH, BufferedImage.TYPE_INT_RGB);
    private final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private final int[] colors = new int[256];

    // I/O
    public final InputHandler input = new InputHandler();
    public final SaveManager saveManager = new SaveManager(this);
    public final GameConfig config = new GameConfig();

    // Variables & Flags
    private int tickCount = 0;
    private int wonTimer = 0;
    private int playerDeadTime = 0;
    private int pendingLevelChange;

    public int gameTime = 0;
    public boolean hasWon = false;
    public boolean loaded = false;

    public static void main(String[] args) {
        new Game(640, 360 + 2).startLoop();
    }

    public Game(int width, int height) {
        super(width, height);
    }

    @Override
    protected void init() {
        int pp = 0;
        for (int r = 0; r < 6; r++) {
            for (int g = 0; g < 6; g++) {
                for (int b = 0; b < 6; b++) {
                    int rr = (r * 255 / 5);
                    int gg = (g * 255 / 5);
                    int bb = (b * 255 / 5);
                    int mid = (rr * 30 + gg * 59 + bb * 11) / 100;

                    int r1 = ((rr + mid * 1) / 2) * 230 / 255 + 10;
                    int g1 = ((gg + mid * 1) / 2) * 230 / 255 + 10;
                    int b1 = ((bb + mid * 1) / 2) * 230 / 255 + 10;
                    colors[pp++] = r1 << 16 | g1 << 8 | b1;
                }
            }
        }

        try {
            screen = new Screen(VIEW_WIDTH, VIEW_HEIGTH, new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("res/icons.png"))));
            lightScreen = new Screen(VIEW_WIDTH, VIEW_HEIGTH, new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("res/icons.png"))));
        } catch (IOException ex) {
            // TODO: better error handling
            JOptionPane.showMessageDialog(null,
                    "Error loading game textures, the game will now close.",
                    "Error Loading Textures",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        frame.addKeyListener(input);
        frame.setTitle("Minicraft_J2DGL");

        // Make save folder if it doesn't exist.
        new File("./saves").mkdir();

        menu = new TitleMenu(this, input);
    }

    //<editor-fold defaultstate="collapsed" desc="public GameState getGameState()">
    public GameState getGameState() {
        GameState gameState = new GameState();
        gameState.playerDeadTime = playerDeadTime;
        gameState.wonTimer = wonTimer;
        gameState.gameTime = gameTime;
        gameState.hasWon = hasWon;
        gameState.levels = levels;
        gameState.level = level;
        gameState.currentLevel = currentLevel;
        gameState.player = player;
        return gameState;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="public void setGameState(GameState gameState)">
    public void setGameState(GameState gameState) {
        playerDeadTime = gameState.playerDeadTime;
        wonTimer = gameState.wonTimer;
        gameTime = gameState.gameTime;
        hasWon = gameState.hasWon;
        currentLevel = gameState.currentLevel;
        levels = gameState.levels;
        level = gameState.level;
        player = gameState.player;
        // Cannot persist game and input.
        player.game = this;
        player.input = input;
        loaded = true;
    }
    //</editor-fold>

    @Override
    protected void update() {
        tickCount++;
        if (!frame.isFocusOwner()) {
            // Game pauses if frame is not in focus.
            input.releaseAll();
        } else {
            if (loaded && !player.removed && !hasWon) {
                gameTime++;
            }
            input.tick();
            if (menu != null) {
                menu.tick();
                if (input.mainMenu.clicked && loaded) {
                    if (menu instanceof TitleMenu) {
                        removeMenu();
                    } else {
                        menu = new TitleMenu(this, input);
                    }
                }
            } else {
                // Game pauses if in menu.
                if (player.removed) {
                    playerDeadTime++;
                    if (playerDeadTime > 60) {
                        menu = new DeadMenu(this, input);
                    }
                } else {
                    if (pendingLevelChange != 0) {
                        menu = new LevelTransitionMenu(pendingLevelChange, this, input);
                        pendingLevelChange = 0;
                    }
                }
                if (wonTimer > 0) {
                    if (--wonTimer == 0) {
                        menu = new WonMenu(this, input);
                    }
                }
                level.tick();
                Tile.tickCount++;
            }
        }
    }

    public void changeLevel(int dir) {
        level.remove(player);
        currentLevel += dir;
        level = levels[currentLevel];
        player.x = (player.x >> 4) * 16 + 8;
        player.y = (player.y >> 4) * 16 + 8;
        level.add(player);
    }

    public void scheduleLevelChange(int dir) {
        pendingLevelChange = dir;
    }

    public void won() {
        wonTimer = 60 * 3;
        hasWon = true;
    }

    public void removeMenu() {
        menu = null;
    }

    @Override
    protected void draw(Graphics2D g2) {
        if (loaded) {
            // Determine current viewport.
            int xScroll = player.x - screen.w / 2;
            int yScroll = player.y - (screen.h - 16) / 2;
            // Prevent viewport from exiting level bounds.
            if (xScroll < 16) {
                xScroll = 16;
            }
            if (yScroll < 16) {
                yScroll = 16;
            }
            if (xScroll > level.width * 16 - screen.w - 16) {
                xScroll = level.width * 16 - screen.w - 16;
            }
            if (yScroll > level.height * 16 - screen.h - 16) {
                yScroll = level.height * 16 - screen.h - 16;
            }
            // ???
            if (currentLevel > 3) {
                int col = Color.get(20, 20, 121, 121);
                for (int y = 0; y < 14; y++) {
                    for (int x = 0; x < 24; x++) {
                        screen.render(x * 8 - ((xScroll / 4) & 7), y * 8 - ((yScroll / 4) & 7), 0, col, 0);
                    }
                }
            }
            // Render background and sprites.
            level.renderBackground(screen, xScroll, yScroll);
            level.renderSprites(screen, xScroll, yScroll);
            // ???
            if (currentLevel < 3) {
                lightScreen.clear(0);
                level.renderLight(lightScreen, xScroll, yScroll);
                screen.overlay(lightScreen, xScroll, yScroll);
            }
        }
        // Render GUI
        renderGui();
        // Render Focus Nagger
        if (!frame.isFocusOwner()) {
            renderFocusNagger();
        }
        // Make the render image's pixels equal to the screen's pixels.
        for (int y = 0; y < screen.h; y++) {
            for (int x = 0; x < screen.w; x++) {
                int cc = screen.pixels[x + y * screen.w];
                if (cc < 255) {
                    pixels[x + y * VIEW_WIDTH] = colors[cc];
                }
            }
        }
        // Draw the render image.
        g2.fillRect(0, 0, resolution.width, resolution.height);
        g2.drawImage(image, 0, 0, 640, 360, null);
    }

    private void renderGui() {
        // Bottom black bar
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 40; x++) {
                screen.render(x * 8, screen.h - 16 + y * 8, 0 + 12 * 32, Color.get(000, 000, 000, 000), 0);
            }
        }
        // Player stats
        if (loaded) {
            for (int i = 0; i < 10; i++) {
                if (i < player.health) {
                    screen.render(i * 8, screen.h - 16, 0 + 12 * 32, Color.get(000, 200, 500, 533), 0);
                } else {
                    screen.render(i * 8, screen.h - 16, 0 + 12 * 32, Color.get(000, 100, 000, 000), 0);
                }
                if (player.staminaRechargeDelay > 0) {
                    if (player.staminaRechargeDelay / 4 % 2 == 0) {
                        screen.render(screen.w - i * 8, screen.h - 16, 1 + 12 * 32, Color.get(000, 555, 000, 000), 0);
                    } else {
                        screen.render(screen.w - i * 8, screen.h - 16, 1 + 12 * 32, Color.get(000, 110, 000, 000), 0);
                    }
                } else {
                    if (i < player.stamina) {
                        screen.render(screen.w - i * 8, screen.h - 16, 1 + 12 * 32, Color.get(000, 220, 550, 553), 0);
                    } else {
                        screen.render(screen.w - i * 8, screen.h - 16, 1 + 12 * 32, Color.get(000, 110, 000, 000), 0);
                    }
                }
            }
            if (player.activeItem != null) {
                player.activeItem.renderInventory(screen, 10 * 8, screen.h - 16);
            }
        }
        // Menu
        if (menu != null) {
            menu.render(screen);
        }
    }

    private void renderFocusNagger() {
        String msg = "Click to focus!";
        int xx = (VIEW_WIDTH - msg.length() * 8) / 2;
        int yy = (VIEW_HEIGTH - 8) / 2;
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

        // Blink effect
        if ((tickCount / 20) % 2 == 0) {
            Font.draw(msg, screen, xx, yy, Color.get(5, 333, 333, 333));
        } else {
            Font.draw(msg, screen, xx, yy, Color.get(5, 555, 555, 555));
        }
    }

    @Override
    protected void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    protected void beforeClose() {

    }

    @Override
    protected void keysPressed(ArrayList<Integer> keyQueue) {

    }

    @Override
    protected void processMouse(MouseEvent mouseEvent) {

    }

}
