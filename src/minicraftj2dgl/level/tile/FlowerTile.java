package minicraftj2dgl.level.tile;

import minicraftj2dgl.entity.ItemEntity;
import minicraftj2dgl.entity.mob.Mob;
import minicraftj2dgl.entity.mob.Player;
import minicraftj2dgl.gfx.Color;
import minicraftj2dgl.gfx.Screen;
import minicraftj2dgl.item.Item;
import minicraftj2dgl.item.ResourceItem;
import minicraftj2dgl.item.ToolItem;
import minicraftj2dgl.item.ToolType;
import minicraftj2dgl.item.resource.Resource;
import minicraftj2dgl.level.Level;

public class FlowerTile extends GrassTile {

    public FlowerTile(int id) {
        super(id);
        tiles[id] = this;
        connectsToGrass = true;
    }

    @Override
    public void render(Screen screen, Level level, int x, int y) {
        super.render(screen, level, x, y);

        int data = level.getData(x, y);
        int shape = (data / 16) % 2;
        int flowerCol = Color.get(10, level.grassColor, 555, 440);

        if (shape == 0) {
            screen.render(x * 16 + 0, y * 16 + 0, 1 + 1 * 32, flowerCol, 0);
        }
        if (shape == 1) {
            screen.render(x * 16 + 8, y * 16 + 0, 1 + 1 * 32, flowerCol, 0);
        }
        if (shape == 1) {
            screen.render(x * 16 + 0, y * 16 + 8, 1 + 1 * 32, flowerCol, 0);
        }
        if (shape == 0) {
            screen.render(x * 16 + 8, y * 16 + 8, 1 + 1 * 32, flowerCol, 0);
        }
    }

    @Override
    public boolean interact(Level level, int x, int y, Player player, Item item, int attackDir) {
        if (item instanceof ToolItem) {
            ToolItem tool = (ToolItem) item;
            if (tool.type.equals(ToolType.shovel)) {
                if (player.payStamina(4 - tool.level)) {
                    level.add(new ItemEntity(new ResourceItem(Resource.flower), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
                    level.add(new ItemEntity(new ResourceItem(Resource.flower), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
                    level.setTile(x, y, Tile.grass, 0);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void hurt(Level level, int x, int y, Mob source, int dmg, int attackDir) {
        int count = random.nextInt(2) + 1;
        for (int i = 0; i < count; i++) {
            level.add(new ItemEntity(new ResourceItem(Resource.flower), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
        }
        level.setTile(x, y, Tile.grass, 0);
    }
}
