package minicraftj2dgl.entity.furnature;

import minicraftj2dgl.entity.mob.Player;
import minicraftj2dgl.crafting.Crafting;
import minicraftj2dgl.gfx.Color;
import minicraftj2dgl.screen.CraftingMenu;

public class Anvil extends Furniture {

    public Anvil() {
        super("Anvil");
        col = Color.get(-1, 000, 111, 222);
        sprite = 0;
        xr = 3;
        yr = 2;
    }

    @Override
    public boolean use(Player player, int attackDir) {
        player.game.menu = new CraftingMenu(Crafting.anvilRecipes, player, player.game, player.input);
        return true;
    }
}
