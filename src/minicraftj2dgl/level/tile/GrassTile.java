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

public class GrassTile extends Tile {

    public GrassTile(int id) {
        super(id);
        connectsToGrass = true;
        }

    @Override
    public void render(Screen screen, Level level, int x, int y) {
        int col = Color.get(level.grassColor, level.grassColor, level.grassColor + 111, level.grassColor + 111);
        int transitionColor = Color.get(level.grassColor - 111, level.grassColor, level.grassColor + 111, level.dirtColor);

        boolean connectsUp = !level.getTile(x, y - 1).connectsToGrass;
        boolean connectsDown = !level.getTile(x, y + 1).connectsToGrass;
        boolean connectsLeft = !level.getTile(x - 1, y).connectsToGrass;
        boolean ConnectsRight = !level.getTile(x + 1, y).connectsToGrass;

        // Draw 4 corners of the tile.
        if (!connectsUp && !connectsLeft) {
            screen.render(x * 16 + 0, y * 16 + 0, 0, col, 0);
        } else {
            screen.render(x * 16 + 0, y * 16 + 0, (connectsLeft ? 11 : 12) + (connectsUp ? 0 : 1) * 32, transitionColor, 0);
        }
        if (!connectsUp && !ConnectsRight) {
            screen.render(x * 16 + 8, y * 16 + 0, 1, col, 0);
        } else {
            screen.render(x * 16 + 8, y * 16 + 0, (ConnectsRight ? 13 : 12) + (connectsUp ? 0 : 1) * 32, transitionColor, 0);
        }
        if (!connectsDown && !connectsLeft) {
            screen.render(x * 16 + 0, y * 16 + 8, 2, col, 0);
        } else {
            screen.render(x * 16 + 0, y * 16 + 8, (connectsLeft ? 11 : 12) + (connectsDown ? 2 : 1) * 32, transitionColor, 0);
        }
        if (!connectsDown && !ConnectsRight) {
            screen.render(x * 16 + 8, y * 16 + 8, 3, col, 0);
        } else {
            screen.render(x * 16 + 8, y * 16 + 8, (ConnectsRight ? 13 : 12) + (connectsDown ? 2 : 1) * 32, transitionColor, 0);
        }
    }

    @Override
    public void tick(Level level, int xt, int yt) {
        if (random.nextInt(40) != 0) {
            return;
        }

        int xn = xt;
        int yn = yt;

        if (random.nextBoolean()) {
            xn += random.nextInt(2) * 2 - 1;
        } else {
            yn += random.nextInt(2) * 2 - 1;
        }

        if (level.getTile(xn, yn) == Tile.dirt) {
            level.setTile(xn, yn, this, 0);
        }
    }

    @Override
    public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) {
        if (item instanceof ToolItem) {
            ToolItem tool = (ToolItem) item;
            if (tool.type.equals(ToolType.shovel)) {
                if (player.payStamina(4 - tool.level)) {
                    level.setTile(xt, yt, Tile.dirt, 0);
                    Sound.monsterHurt.play();
                    if (random.nextInt(5) == 0) {
                        level.add(new ItemEntity(new ResourceItem(Resource.seeds), xt * 16 + random.nextInt(10) + 3, yt * 16 + random.nextInt(10) + 3));
                        return true;
                    }
                }
            }
            if (tool.type.equals(ToolType.hoe)) {
                if (player.payStamina(4 - tool.level)) {
                    Sound.monsterHurt.play();
                    if (random.nextInt(5) == 0) {
                        level.add(new ItemEntity(new ResourceItem(Resource.seeds), xt * 16 + random.nextInt(10) + 3, yt * 16 + random.nextInt(10) + 3));
                        return true;
                    }
                    level.setTile(xt, yt, Tile.farmland, 0);
                    return true;
                }
            }
        }
        return false;

    }
}
