package minicraftj2dgl.entity.furnature;

import minicraftj2dgl.entity.mob.Player;
import minicraftj2dgl.crafting.Crafting;
import minicraftj2dgl.gfx.Color;
import minicraftj2dgl.screen.CraftingMenu;

public class Furnace extends Furniture {

    public Furnace() {
        super("Furnace");
        col = Color.get(-1, 000, 222, 333);
        sprite = 3;
        xr = 3;
        yr = 2;
    }

    @Override
    public boolean use(Player player, int attackDir) {
        player.game.menu = new CraftingMenu(Crafting.furnaceRecipes, player, player.game, player.input);
        return true;
    }
}
