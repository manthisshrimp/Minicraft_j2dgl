package minicraftj2dgl.screen;

import minicraftj2dgl.Game;
import minicraftj2dgl.InputHandler;
import minicraftj2dgl.entity.mob.Player;
import minicraftj2dgl.gfx.Font;
import minicraftj2dgl.gfx.Screen;
import minicraftj2dgl.item.Item;

public class InventoryMenu extends Menu {

    private final Player player;
    private int selected = 0;

    public InventoryMenu(Player player, Game game, InputHandler inputHandler) {
        super(game, inputHandler);
        this.player = player;

        if (player.activeItem != null) {
            player.inventory.items.add(0, player.activeItem);
            player.activeItem = null;
        }
    }

    @Override
    public void tick() {
        if (input.menu.clicked) {
            game.removeMenu();
        }

        if (input.up.clicked) {
            selected--;
        }
        if (input.down.clicked) {
            selected++;
        }

        int len = player.inventory.items.size();
        if (len == 0) {
            selected = 0;
        }
        if (selected < 0) {
            selected += len;
        }
        if (selected >= len) {
            selected -= len;
        }

        if (input.attack.clicked && len > 0) {
            Item item = player.inventory.items.remove(selected);
            player.activeItem = item;
            game.removeMenu();
        }
    }

    @Override
    public void render(Screen screen) {
        Font.renderFrame(screen, "inventory", 1, 1, 12, 11);
        renderItemList(screen, 1, 1, 12, 11, player.inventory.items, selected);
    }
}
