package minicraftj2dgl.entity;

import minicraftj2dgl.entity.mob.Mob;
import minicraftj2dgl.entity.mob.Player;
import minicraftj2dgl.gfx.Color;
import minicraftj2dgl.gfx.Screen;
import minicraftj2dgl.item.Item;
import minicraftj2dgl.sound.Sound;

public class ItemEntity extends Entity {

    private final int lifeTime;
    protected int walkDist = 0;
    protected int dir = 0;
    public int hurtTime = 0;
    protected int xKnockback, yKnockback;
    public double xa, ya, za;
    public double xx, yy, zz;
    public Item item;
    private int time = 0;

    public ItemEntity(Item item, int x, int y) {
        this.item = item;
        xx = this.x = x;
        yy = this.y = y;
        xr = 3;
        yr = 3;

        zz = 2;
        xa = random.nextGaussian() * 0.3;
        ya = random.nextGaussian() * 0.2;
        za = random.nextFloat() * 0.7 + 1;

        lifeTime = 60 * 10 + random.nextInt(60);
    }

    @Override
    public void tick() {
        time++;
        if (time >= lifeTime) {
            remove();
            return;
        }
        xx += xa;
        yy += ya;
        zz += za;
        if (zz < 0) {
            zz = 0;
            za *= -0.5;
            xa *= 0.6;
            ya *= 0.6;
        }
        za -= 0.15;
        int ox = x;
        int oy = y;
        int nx = (int) xx;
        int ny = (int) yy;
        int expectedx = nx - x;
        int expectedy = ny - y;
        move(nx - x, ny - y);
        int gotx = x - ox;
        int goty = y - oy;
        xx += gotx - expectedx;
        yy += goty - expectedy;

        if (hurtTime > 0) {
            hurtTime--;
        }
    }

    @Override
    public boolean isBlockableBy(Mob mob) {
        return false;
    }

    @Override
    public void render(Screen screen) {
        if (time >= lifeTime - 6 * 20) {
            if (time / 6 % 2 == 0) {
                return;
            }
        }
        screen.render(x - 4, y - 4, item.getSprite(), Color.get(-1, 0, 0, 0), 0);
        screen.render(x - 4, y - 4 - (int) (zz), item.getSprite(), item.getColor(), 0);
    }

    @Override
    public void touchedBy(Entity entity) {
        if (time > 30) {
            entity.touchItem(this);
        }
    }

    public void take(Player player) {
        Sound.pickup.play();
        player.score++;
        item.onTake(this);
        remove();
    }
}