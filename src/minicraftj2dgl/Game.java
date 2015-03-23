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
import java.util.Random;
import javax.imageio.ImageIO;
import minicraftj2dgl.entity.mob.Player;
import minicraftj2dgl.gfx.Color;
import minicraftj2dgl.gfx.Font;
import minicraftj2dgl.gfx.Screen;
import minicraftj2dgl.gfx.SpriteSheet;
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

    private final Random random = new Random();

    private Screen screen;
    private Screen lightScreen;
    public Player player;
    public Menu menu;
    private int playerDeadTime;
    private int pendingLevelChange;
    private boolean playAble = false;

    private final int VIEW_WIDTH = 640 / 2;
    private final int VIEW_HEIGTH = 360 / 2;

    private final BufferedImage image = new BufferedImage(VIEW_WIDTH, VIEW_HEIGTH, BufferedImage.TYPE_INT_RGB);
    private final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    public final minicraftj2dgl.InputHandler input = new InputHandler();

    public final SaveManager saveManager = new SaveManager(this);
    private final int[] colors = new int[256];
    private int tickCount = 0;
    public int gameTime = 0;
    private Level[] levels = new Level[5];
    private int currentLevel = 3;
    private Level level = levels[currentLevel];
    private int wonTimer = 0;
    public boolean hasWon = false;

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
        } catch (IOException e) {
        }

        frame.addKeyListener(input);

        // Make save folder if it doesn't exist.
        new File("./saves").mkdir();

        // TODO: remove resetGame(); and fix conditional rendering.
        resetGame();
        menu = new TitleMenu(this, input);
    }

    public GameState getGameState() {
        GameState gameState = new GameState();

        gameState.playerDeadTime = playerDeadTime;
        gameState.wonTimer = wonTimer;
        gameState.gameTime = gameTime;
        gameState.hasWon = hasWon;
        gameState.levels = levels;
        gameState.currentLevel = currentLevel;
        gameState.player = player;

        return gameState;
    }

    public GameState setGameState(GameState gameState) {
        playerDeadTime = gameState.playerDeadTime;
        wonTimer = gameState.wonTimer;
        gameTime = gameState.gameTime;
        hasWon = gameState.hasWon;
        currentLevel = gameState.currentLevel;

        levels = gameState.levels;
        level = levels[currentLevel];

        player = gameState.player;
        player.game = this;
        player.input = input;

        playAble = true;

        return gameState;
    }

    public void resetGame() {
        playerDeadTime = 0;
        wonTimer = 0;
        gameTime = 0;
        hasWon = false;

        levels = new Level[5];
        currentLevel = 3;

        levels[4] = new Level(128, 128, 1, null);
        levels[3] = new Level(128, 128, 0, levels[4]);
        levels[2] = new Level(128, 128, -1, levels[3]);
        levels[1] = new Level(128, 128, -2, levels[2]);
        levels[0] = new Level(128, 128, -3, levels[1]);

        level = levels[currentLevel];
        player = new Player(this, input);
        player.findStartPos(level);

        level.add(player);

        for (int i = 0; i < 5; i++) {
            levels[i].trySpawn(5000);
        }

        playAble = true;
    }

    @Override
    protected void update() {
        tickCount++;
        if (!frame.isFocusOwner()) {
            input.releaseAll();
        } else {
            if (!player.removed && !hasWon) {
                gameTime++;
            }

            input.tick();
            if (menu != null) {
                menu.tick();
                if (input.mainMenu.clicked && playAble) {
                    if (menu instanceof TitleMenu) {
                        removeMenu();
                    } else {
                        menu = new TitleMenu(this, input);
                    }
                }
            } else {
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
        int xScroll = player.x - screen.w / 2;
        int yScroll = player.y - (screen.h - 8) / 2;
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
        if (currentLevel > 3) {
            int col = Color.get(20, 20, 121, 121);
            for (int y = 0; y < 14; y++) {
                for (int x = 0; x < 24; x++) {
                    screen.render(x * 8 - ((xScroll / 4) & 7), y * 8 - ((yScroll / 4) & 7), 0, col, 0);
                }
            }
        }

        level.renderBackground(screen, xScroll, yScroll);
        level.renderSprites(screen, xScroll, yScroll);

        if (currentLevel < 3) {
            lightScreen.clear(0);
            level.renderLight(lightScreen, xScroll, yScroll);
            screen.overlay(lightScreen, xScroll, yScroll);
        }

        renderGui();

        if (!frame.isFocusOwner()) {
            renderFocusNagger();
        }
        for (int y = 0; y < screen.h; y++) {
            for (int x = 0; x < screen.w; x++) {
                int cc = screen.pixels[x + y * screen.w];
                if (cc < 255) {
                    pixels[x + y * VIEW_WIDTH] = colors[cc];
                }
            }
        }

        g2.fillRect(0, 0, resolution.width, resolution.height);
        g2.drawImage(image, 0, 0, 640, 360, null);
    }

    private void renderGui() {
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 20; x++) {
                screen.render(x * 8, screen.h - 16 + y * 8, 0 + 12 * 32, Color.get(000, 000, 000, 000), 0);
            }
        }

        for (int i = 0; i < 10; i++) {
            if (i < player.health) {
                screen.render(i * 8, screen.h - 16, 0 + 12 * 32, Color.get(000, 200, 500, 533), 0);
            } else {
                screen.render(i * 8, screen.h - 16, 0 + 12 * 32, Color.get(000, 100, 000, 000), 0);
            }

            if (player.staminaRechargeDelay > 0) {
                if (player.staminaRechargeDelay / 4 % 2 == 0) {
                    screen.render(i * 8, screen.h - 8, 1 + 12 * 32, Color.get(000, 555, 000, 000), 0);
                } else {
                    screen.render(i * 8, screen.h - 8, 1 + 12 * 32, Color.get(000, 110, 000, 000), 0);
                }
            } else {
                if (i < player.stamina) {
                    screen.render(i * 8, screen.h - 8, 1 + 12 * 32, Color.get(000, 220, 550, 553), 0);
                } else {
                    screen.render(i * 8, screen.h - 8, 1 + 12 * 32, Color.get(000, 110, 000, 000), 0);
                }
            }
        }
        if (player.activeItem != null) {
            player.activeItem.renderInventory(screen, 10 * 8, screen.h - 16);
        }

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