package minicraftj2dgl.item;

import java.io.Serializable;
import minicraftj2dgl.entity.Entity;
import minicraftj2dgl.entity.ItemEntity;
import minicraftj2dgl.entity.mob.Player;
import minicraftj2dgl.gfx.Screen;
import minicraftj2dgl.level.Level;
import minicraftj2dgl.level.tile.Tile;
import minicraftj2dgl.screen.ListItem;

public class Item implements ListItem, Serializable {

    public int getColor() {
        return 0;
    }

    public int getSprite() {
        return 0;
    }

    public void onTake(ItemEntity itemEntity) {
    }

    @Override
    public void renderInventory(Screen screen, int x, int y) {
    }

    public boolean interact(Player player, Entity entity, int attackDir) {
        return false;
    }

    public void renderIcon(Screen screen, int x, int y) {
    }

    public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir) {
        return false;
    }

    public boolean isDepleted() {
        return false;
    }

    public boolean canAttack() {
        return false;
    }

    public int getAttackDamageBonus(Entity e) {
        return 0;
    }

    public String getName() {
        return "";
    }

    public boolean matches(Item item) {
        return item.getClass().equals(this.getClass());
    }
}
