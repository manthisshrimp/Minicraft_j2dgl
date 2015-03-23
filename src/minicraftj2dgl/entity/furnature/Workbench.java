package minicraftj2dgl.entity.furnature;

import minicraftj2dgl.entity.mob.Player;
import minicraftj2dgl.crafting.Crafting;
import minicraftj2dgl.gfx.Color;
import minicraftj2dgl.screen.CraftingMenu;

public class Workbench extends Furniture {

    public Workbench() {
        super("Workbench");
        col = Color.get(-1, 100, 321, 431);
        sprite = 4;
        xr = 3;
        yr = 2;
    }

    @Override
    public boolean use(Player player, int attackDir) {
        player.game.menu = new CraftingMenu(Crafting.workbenchRecipes, player, player.game, player.input);
        return true;
    }
}
