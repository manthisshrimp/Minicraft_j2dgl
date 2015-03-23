package minicraftj2dgl.entity.particle;

import minicraftj2dgl.entity.Entity;
import minicraftj2dgl.gfx.Color;
import minicraftj2dgl.gfx.Screen;
import minicraftj2dgl.sound.Sound;

public class SmashParticle extends Entity {

    private int time = 0;

    public SmashParticle(int x, int y) {
        this.x = x;
        this.y = y;
        Sound.monsterHurt.play();
    }

    @Override
    public void tick() {
        time++;
        if (time > 10) {
            remove();
        }
    }

    @Override
    public void render(Screen screen) {
        int col = Color.get(-1, 555, 555, 555);
        screen.render(x - 8, y - 8, 5 + 12 * 32, col, 2);
        screen.render(x - 0, y - 8, 5 + 12 * 32, col, 3);
        screen.render(x - 8, y - 0, 5 + 12 * 32, col, 0);
        screen.render(x - 0, y - 0, 5 + 12 * 32, col, 1);
    }
}
