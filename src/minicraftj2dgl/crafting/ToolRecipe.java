package minicraftj2dgl.crafting;

import minicraftj2dgl.entity.mob.Player;
import minicraftj2dgl.item.ToolItem;
import minicraftj2dgl.item.ToolType;

public class ToolRecipe extends Recipe {

    private final ToolType type;
    private final int level;

    public ToolRecipe(ToolType type, int level) {
        super(new ToolItem(type, level));
        this.type = type;
        this.level = level;
    }

    @Override
    public void craft(Player player) {
        player.inventory.add(0, new ToolItem(type, level));
    }
}
