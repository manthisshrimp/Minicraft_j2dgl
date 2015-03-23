package minicraftj2dgl.level.tile;

import minicraftj2dgl.entity.ItemEntity;
import minicraftj2dgl.entity.mob.Player;
import minicraftj2dgl.gfx.Color;
import minicraftj2dgl.gfx.Screen;
import minicraftj2dgl.item.Item;
import minicraftj2dgl.item.ResourceItem;
import minicraftj2dgl.item.ToolItem;
import minicraftj2dgl.item.ToolType;
import minicraftj2dgl.item.resource.Resource;
import minicraftj2dgl.level.Level;
import minicraftj2dgl.sound.Sound;

public class DirtTile extends Tile {

    public DirtTile(int id) {
        super(id);
    }

    @Override
    public void render(Screen screen, Level level, int x, int y) {
        int col = Color.get(level.dirtColor, level.dirtColor, level.dirtColor - 111, level.dirtColor - 111);
        screen.render(x * 16 + 0, y * 16 + 0, 0, col, 0);
        screen.render(x * 16 + 8, y * 16 + 0, 1, col, 0);
        screen.render(x * 16 + 0, y * 16 + 8, 2, col, 0);
        screen.render(x * 16 + 8, y * 16 + 8, 3, col, 0);
    }

    @Override
    public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) {
        if (item instanceof ToolItem) {
            ToolItem tool = (ToolItem) item;
            if (tool.type.equals(ToolType.shovel)) {
                if (player.payStamina(4 - tool.level)) {
                    level.setTile(xt, yt, Tile.hole, 0);
                    level.add(new ItemEntity(new ResourceItem(Resource.dirt), xt * 16 + random.nextInt(10) + 3, yt * 16 + random.nextInt(10) + 3));
                    Sound.monsterHurt.play();
                    return true;
                }
            }
            if (tool.type.equals(ToolType.hoe)) {
                if (player.payStamina(4 - tool.level)) {
                    level.setTile(xt, yt, Tile.farmland, 0);
                    Sound.monsterHurt.play();
                    return true;
                }
            }
        }
        return false;
    }
}
