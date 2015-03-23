package minicraftj2dgl.item;

import minicraftj2dgl.entity.ItemEntity;
import minicraftj2dgl.entity.mob.Player;
import minicraftj2dgl.gfx.Color;
import minicraftj2dgl.gfx.Font;
import minicraftj2dgl.gfx.Screen;
import minicraftj2dgl.item.resource.Resource;
import minicraftj2dgl.level.Level;
import minicraftj2dgl.level.tile.Tile;

public class ResourceItem extends Item {

    public Resource resource;
    public int count = 1;

    public ResourceItem(Resource resource) {
        this.resource = resource;
    }

    public ResourceItem(Resource resource, int count) {
        this.resource = resource;
        this.count = count;
    }

    @Override
    public int getColor() {
        return resource.color;
    }

    @Override
    public int getSprite() {
        return resource.sprite;
    }

    @Override
    public void renderIcon(Screen screen, int x, int y) {
        screen.render(x, y, resource.sprite, resource.color, 0);
    }

    @Override
    public void renderInventory(Screen screen, int x, int y) {
        screen.render(x, y, resource.sprite, resource.color, 0);
        Font.draw(resource.name, screen, x + 32, y, Color.get(-1, 555, 555, 555));
        int cc = count;
        if (cc > 999) {
            cc = 999;
        }
        Font.draw("" + cc, screen, x + 8, y, Color.get(-1, 444, 444, 444));
    }

    @Override
    public String getName() {
        return resource.name;
    }

    @Override
    public void onTake(ItemEntity itemEntity) {
    }

    @Override
    public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir) {
        if (resource.interactOn(tile, level, xt, yt, player, attackDir)) {
            count--;
            return true;
        }
        return false;
    }

    @Override
    public boolean isDepleted() {
        return count <= 0;
    }

}
