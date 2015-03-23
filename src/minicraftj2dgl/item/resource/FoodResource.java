package minicraftj2dgl.item.resource;

import minicraftj2dgl.entity.mob.Player;
import minicraftj2dgl.level.Level;
import minicraftj2dgl.level.tile.Tile;

public class FoodResource extends Resource {

    private final int heal;
    private final int staminaCost;

    public FoodResource(String name, int sprite, int color, int heal, int staminaCost) {
        super(name, sprite, color);
        this.heal = heal;
        this.staminaCost = staminaCost;
    }

    @Override
    public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir) {
        if (player.health < player.maxHealth && player.payStamina(staminaCost)) {
            player.heal(heal);
            return true;
        }
        return false;
    }
}
