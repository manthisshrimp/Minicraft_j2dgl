package minicraftj2dgl.crafting;

import minicraftj2dgl.entity.furnature.Furniture;
import minicraftj2dgl.entity.mob.Player;
import minicraftj2dgl.item.FurnitureItem;

public class FurnitureRecipe extends Recipe {

    private final Class<? extends Furniture> clazz;

    public FurnitureRecipe(Class<? extends Furniture> clazz) throws InstantiationException, IllegalAccessException {
        super(new FurnitureItem(clazz.newInstance()));
        this.clazz = clazz;
    }

    @Override
    public void craft(Player player) {
        try {
            player.inventory.add(0, new FurnitureItem(clazz.newInstance()));
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
