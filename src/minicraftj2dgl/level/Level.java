package minicraftj2dgl.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import minicraftj2dgl.entity.mob.AirWizard;
import minicraftj2dgl.entity.Entity;
import minicraftj2dgl.entity.mob.Mob;
import minicraftj2dgl.entity.mob.Player;
import minicraftj2dgl.entity.mob.Slime;
import minicraftj2dgl.entity.mob.Zombie;
import minicraftj2dgl.gfx.Screen;
import minicraftj2dgl.level.levelgen.LevelGen;
import minicraftj2dgl.level.tile.Tile;
import java.io.Serializable;

public class Level implements Serializable {

    private final Random random = new Random();
    public int grassColor = 141;
    public int dirtColor = 322;
    public int sandColor = 550;
    public int monsterDensity = 8;
    private final SpriteSorter spriteSorter = new SpriteSorter();

    public int width, height;
    public byte[] tiles;
    public byte[] data;
    public List<Entity>[] entitiesInTiles;
    public List<Entity> entities = new ArrayList<>();
    private final List<Entity> rowSprites = new ArrayList<>();
    private final int depth;
    public Player player;

    public Level(int w, int h, int level, Level parentLevel) {
        if (level < 0) {
            dirtColor = 222;
        }
        this.depth = level;
        this.width = w;
        this.height = h;
        byte[][] maps;

        if (level == 1) {
            dirtColor = 444;
        }
        if (level == 0) {
            maps = LevelGen.createAndValidateTopMap(w, h);
        } else if (level < 0) {
            maps = LevelGen.createAndValidateUndergroundMap(w, h, -level);
            monsterDensity = 4;
        } else {
            maps = LevelGen.createAndValidateSkyMap(w, h);
            monsterDensity = 4;
        }

        tiles = maps[0];
        data = maps[1];

        if (parentLevel != null) {
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    if (parentLevel.getTile(x, y) == Tile.stairsDown) {

                        setTile(x, y, Tile.stairsUp, 0);
                        if (level == 0) {
                            setTile(x - 1, y, Tile.hardRock, 0);
                            setTile(x + 1, y, Tile.hardRock, 0);
                            setTile(x, y - 1, Tile.hardRock, 0);
                            setTile(x, y + 1, Tile.hardRock, 0);
                            setTile(x - 1, y - 1, Tile.hardRock, 0);
                            setTile(x - 1, y + 1, Tile.hardRock, 0);
                            setTile(x + 1, y - 1, Tile.hardRock, 0);
                            setTile(x + 1, y + 1, Tile.hardRock, 0);
                        } else {
                            setTile(x - 1, y, Tile.dirt, 0);
                            setTile(x + 1, y, Tile.dirt, 0);
                            setTile(x, y - 1, Tile.dirt, 0);
                            setTile(x, y + 1, Tile.dirt, 0);
                            setTile(x - 1, y - 1, Tile.dirt, 0);
                            setTile(x - 1, y + 1, Tile.dirt, 0);
                            setTile(x + 1, y - 1, Tile.dirt, 0);
                            setTile(x + 1, y + 1, Tile.dirt, 0);
                        }
                    }

                }
            }
        }

        entitiesInTiles = new ArrayList[w * h];
        for (int i = 0; i < w * h; i++) {
            entitiesInTiles[i] = new ArrayList<>();
        }

        if (level == 1) {
            AirWizard aw = new AirWizard();
            aw.x = w * 8;
            aw.y = h * 8;
            add(aw);
        }
    }

    public void renderBackground(Screen screen, int xScroll, int yScroll) {
        int xo = xScroll >> 4;
        int yo = yScroll >> 4;
        int w = (screen.w + 15) >> 4;
        int h = (screen.h + 15) >> 4;
        screen.setOffset(xScroll, yScroll);
        for (int y = yo; y <= h + yo; y++) {
            for (int x = xo; x <= w + xo; x++) {
                getTile(x, y).render(screen, this, x, y);
            }
        }
        screen.setOffset(0, 0);
    }

    public void renderSprites(Screen screen, int xScroll, int yScroll) {
        int xo = xScroll >> 4;
        int yo = yScroll >> 4;
        int w = (screen.w + 15) >> 4;
        int h = (screen.h + 15) >> 4;

        screen.setOffset(xScroll, yScroll);
        for (int y = yo; y <= h + yo; y++) {
            for (int x = xo; x <= w + xo; x++) {
                if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
                    continue;
                }
                rowSprites.addAll(entitiesInTiles[x + y * this.width]);
            }
            if (rowSprites.size() > 0) {
                sortAndRender(screen, rowSprites);
            }
            rowSprites.clear();
        }
        screen.setOffset(0, 0);
    }

    public void renderLight(Screen screen, int xScroll, int yScroll) {
        int xo = xScroll >> 4;
        int yo = yScroll >> 4;
        int w = (screen.w + 15) >> 4;
        int h = (screen.h + 15) >> 4;

        screen.setOffset(xScroll, yScroll);
        int r = 4;
        for (int y = yo - r; y <= h + yo + r; y++) {
            for (int x = xo - r; x <= w + xo + r; x++) {
                if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
                    continue;
                }
                List<Entity> entitiesWhatIsThis = entitiesInTiles[x + y * this.width];
                entitiesWhatIsThis.stream().forEach((e) -> {
                    int lr = e.getLightRadius();
                    if (lr > 0) {
                        screen.renderLight(e.x - 1, e.y - 4, lr * 8);
                    }
                });
                int lr = getTile(x, y).getLightRadius(this, x, y);
                if (lr > 0) {
                    screen.renderLight(x * 16 + 8, y * 16 + 8, lr * 8);
                }
            }
        }
        screen.setOffset(0, 0);
    }

    // private void renderLight(Screen screen, int x, int y, int r) {
    // screen.renderLight(x, y, r);
    // }
    private void sortAndRender(Screen screen, List<Entity> list) {
        Collections.sort(list, spriteSorter);
        list.stream().forEach((list1) -> {
            list1.render(screen);
        });
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return Tile.rock;
        }
        return Tile.tiles[tiles[x + y * width]];
    }

    public final void setTile(int x, int y, Tile t, int dataVal) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return;
        }
        tiles[x + y * width] = t.id;
        data[x + y * width] = (byte) dataVal;
    }

    public int getData(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return 0;
        }
        return data[x + y * width] & 0xff;
    }

    public void setData(int x, int y, int val) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return;
        }
        data[x + y * width] = (byte) val;
    }

    public final void add(Entity entity) {
        if (entity instanceof Player) {
            player = (Player) entity;
        }
        entity.removed = false;
        entities.add(entity);
        entity.init(this);

        insertEntity(entity.x >> 4, entity.y >> 4, entity);
    }

    public void remove(Entity e) {
        entities.remove(e);
        int xto = e.x >> 4;
        int yto = e.y >> 4;
        removeEntity(xto, yto, e);
    }

    private void insertEntity(int x, int y, Entity e) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return;
        }
        entitiesInTiles[x + y * width].add(e);
    }

    private void removeEntity(int x, int y, Entity e) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return;
        }
        entitiesInTiles[x + y * width].remove(e);
    }

    public void trySpawn(int count) {
        for (int i = 0; i < count; i++) {
            Mob mob;

            int minLevel = 1;
            int maxLevel = 1;
            if (depth < 0) {
                maxLevel = (-depth) + 1;
            }
            if (depth > 0) {
                minLevel = maxLevel = 4;
            }

            int lvl = random.nextInt(maxLevel - minLevel + 1) + minLevel;
            if (random.nextInt(2) == 0) {
                mob = new Slime(lvl);
            } else {
                mob = new Zombie(lvl);
            }

            if (mob.findStartPos(this)) {
                this.add(mob);
            }
        }
    }

    public void tick() {
        trySpawn(1);

        for (int i = 0; i < width * height / 50; i++) {
            int xt = random.nextInt(width);
            int yt = random.nextInt(width);
            getTile(xt, yt).tick(this, xt, yt);
        }
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            int xto = entity.x >> 4;
            int yto = entity.y >> 4;

            entity.tick();

            if (entity.removed) {
                entities.remove(i--);
                removeEntity(xto, yto, entity);
            } else {
                int xt = entity.x >> 4;
                int yt = entity.y >> 4;

                if (xto != xt || yto != yt) {
                    removeEntity(xto, yto, entity);
                    insertEntity(xt, yt, entity);
                }
            }
        }
    }

    public List<Entity> getEntities(int x0, int y0, int x1, int y1) {
        List<Entity> result = new ArrayList<>();
        int xt0 = (x0 >> 4) - 1;
        int yt0 = (y0 >> 4) - 1;
        int xt1 = (x1 >> 4) + 1;
        int yt1 = (y1 >> 4) + 1;
        for (int y = yt0; y <= yt1; y++) {
            for (int x = xt0; x <= xt1; x++) {
                if (x < 0 || y < 0 || x >= width || y >= height) {
                    continue;
                }
                List<Entity> entitiesWhatIsThis = entitiesInTiles[x + y * this.width];
                entitiesWhatIsThis.stream().filter((e) -> (e.intersects(x0, y0, x1, y1))).forEach((e) -> {
                    result.add(e);
                });
            }
        }
        return result;
    }

    private class SpriteSorter implements Comparator<Entity>, Serializable {

        @Override
        public int compare(Entity e0, Entity e1) {
            if (e1.y < e0.y) {
                return +1;
            }
            if (e1.y > e0.y) {
                return -1;
            }
            return 0;
        }

    }
}
