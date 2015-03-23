package minicraftj2dgl.crafting;

import java.util.ArrayList;
import java.util.List;

import minicraftj2dgl.entity.mob.Player;
import minicraftj2dgl.gfx.Color;
import minicraftj2dgl.gfx.Font;
import minicraftj2dgl.gfx.Screen;
import minicraftj2dgl.item.Item;
import minicraftj2dgl.item.ResourceItem;
import minicraftj2dgl.item.resource.Resource;
import minicraftj2dgl.screen.ListItem;

public abstract class Recipe implements ListItem {

    public List<Item> costs = new ArrayList<>();
    public boolean canCraft = false;
    public Item resultTemplate;

    public Recipe(Item resultTemplate) {
        this.resultTemplate = resultTemplate;
    }

    public Recipe addCost(Resource resource, int count) {
        costs.add(new ResourceItem(resource, count));
        return this;
    }

    public void checkCanCraft(Player player) {
        for (Item item : costs) {
            if (item instanceof ResourceItem) {
                ResourceItem ri = (ResourceItem) item;
                if (!player.inventory.hasResources(ri.resource, ri.count)) {
                    canCraft = false;
                    return;
                }
            }
        }
        canCraft = true;
    }

    @Override
    public void renderInventory(Screen screen, int x, int y) {
        screen.render(x, y, resultTemplate.getSprite(), resultTemplate.getColor(), 0);
        int textColor = canCraft ? Color.get(-1, 555, 555, 555) : Color.get(-1, 222, 222, 222);
        Font.draw(resultTemplate.getName(), screen, x + 8, y, textColor);
    }

    public abstract void craft(Player player);

    public void deductCost(Player player) {
        for (Item item : costs) {
            if (item instanceof ResourceItem) {
                ResourceItem ri = (ResourceItem) item;
                player.inventory.removeResource(ri.resource, ri.count);
            }
        }
    }
}
