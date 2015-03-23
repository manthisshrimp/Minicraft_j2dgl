package minicraftj2dgl.item;

import minicraftj2dgl.entity.furnature.Furniture;
import minicraftj2dgl.entity.ItemEntity;
import minicraftj2dgl.entity.mob.Player;
import minicraftj2dgl.gfx.Color;
import minicraftj2dgl.gfx.Font;
import minicraftj2dgl.gfx.Screen;
import minicraftj2dgl.level.Level;
import minicraftj2dgl.level.tile.Tile;

public class FurnitureItem extends Item {

    public Furniture furniture;
    public boolean placed = false;

    public FurnitureItem(Furniture furniture) {
        this.furniture = furniture;
    }

    @Override
    public int getColor() {
        return furniture.col;
    }

    @Override
    public int getSprite() {
        return furniture.sprite + 10 * 32;
    }

    @Override
    public void renderIcon(Screen screen, int x, int y) {
        screen.render(x, y, getSprite(), getColor(), 0);
    }

    @Override
    public void renderInventory(Screen screen, int x, int y) {
        screen.render(x, y, getSprite(), getColor(), 0);
        Font.draw(furniture.name, screen, x + 8, y, Color.get(-1, 555, 555, 555));
    }

    @Override
    public void onTake(ItemEntity itemEntity) {
    }

    @Override
    public boolean canAttack() {
        return false;
    }

    @Override
    public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir) {
        if (tile.mayPass(level, xt, yt, furniture)) {
            furniture.x = xt * 16 + 8;
            furniture.y = yt * 16 + 8;
            level.add(furniture);
            placed = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean isDepleted() {
        return placed;
    }

    @Override
    public String getName() {
        return furniture.name;
    }
}
