package minicraftj2dgl.level.tile;

import minicraftj2dgl.entity.Entity;
import minicraftj2dgl.entity.mob.Player;
import minicraftj2dgl.gfx.Color;
import minicraftj2dgl.gfx.Screen;
import minicraftj2dgl.item.Item;
import minicraftj2dgl.item.ToolItem;
import minicraftj2dgl.item.ToolType;
import minicraftj2dgl.level.Level;

public class FarmTile extends Tile {

    public FarmTile(int id) {
        super(id);
    }

    @Override
    public void render(Screen screen, Level level, int x, int y) {
        int col = Color.get(level.dirtColor - 121, level.dirtColor - 11, level.dirtColor, level.dirtColor + 111);
        screen.render(x * 16 + 0, y * 16 + 0, 2 + 32, col, 1);
        screen.render(x * 16 + 8, y * 16 + 0, 2 + 32, col, 0);
        screen.render(x * 16 + 0, y * 16 + 8, 2 + 32, col, 0);
        screen.render(x * 16 + 8, y * 16 + 8, 2 + 32, col, 1);
    }

    @Override
    public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) {
        if (item instanceof ToolItem) {
            ToolItem tool = (ToolItem) item;
            if (tool.type.equals(ToolType.shovel)) {
                if (player.payStamina(4 - tool.level)) {
                    level.setTile(xt, yt, Tile.dirt, 0);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void tick(Level level, int xt, int yt) {
        int age = level.getData(xt, yt);
        if (age < 5) {
            level.setData(xt, yt, age + 1);
        }
    }

    @Override
    public void steppedOn(Level level, int xt, int yt, Entity entity) {
        if (random.nextInt(60) != 0) {
            return;
        }
        if (level.getData(xt, yt) < 5) {
            return;
        }
        level.setTile(xt, yt, Tile.dirt, 0);
    }
}
