package minicraftj2dgl.crafting;

import minicraftj2dgl.entity.mob.Player;
import minicraftj2dgl.item.ResourceItem;
import minicraftj2dgl.item.resource.Resource;

public class ResourceRecipe extends Recipe {

    private final Resource resource;

    public ResourceRecipe(Resource resource) {
        super(new ResourceItem(resource, 1));
        this.resource = resource;
    }

    @Override
    public void craft(Player player) {
        player.inventory.add(0, new ResourceItem(resource, 1));
    }
}
