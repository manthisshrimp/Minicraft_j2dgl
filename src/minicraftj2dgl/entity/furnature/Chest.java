package minicraftj2dgl.entity.furnature;

import minicraftj2dgl.entity.Inventory;
import minicraftj2dgl.entity.mob.Player;
import minicraftj2dgl.gfx.Color;
import minicraftj2dgl.screen.ContainerMenu;

public class Chest extends Furniture {

    public Inventory inventory = new Inventory();

    public Chest() {
        super("Chest");
        col = Color.get(-1, 110, 331, 552);
        sprite = 1;
    }

    @Override
    public boolean use(Player player, int attackDir) {
        player.game.menu = new ContainerMenu(player, "Chest", inventory, player.game, player.input);
        return true;
    }
}
