package minicraftj2dgl.entity.furnature;

import minicraftj2dgl.gfx.Color;

public class Lantern extends Furniture {

    public Lantern() {
        super("Lantern");
        col = Color.get(-1, 000, 111, 555);
        sprite = 5;
        xr = 3;
        yr = 2;
    }

    @Override
    public int getLightRadius() {
        return 8;
    }
}
