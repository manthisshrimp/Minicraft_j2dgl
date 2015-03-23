package minicraftj2dgl.level.tile;

import minicraftj2dgl.entity.mob.AirWizard;
import minicraftj2dgl.entity.Entity;
import minicraftj2dgl.gfx.Screen;
import minicraftj2dgl.level.Level;

public class InfiniteFallTile extends Tile {

    public InfiniteFallTile(int id) {
        super(id);
    }

    @Override
    public void render(Screen screen, Level level, int x, int y) {
    }

    @Override
    public void tick(Level level, int xt, int yt) {
    }

    @Override
    public boolean mayPass(Level level, int x, int y, Entity entity) {
        return entity instanceof AirWizard;
    }
}
