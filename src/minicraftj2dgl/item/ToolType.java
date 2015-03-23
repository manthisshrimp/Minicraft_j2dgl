package minicraftj2dgl.item;

import java.io.Serializable;
import java.util.Objects;

public class ToolType implements Serializable {

    public static ToolType shovel = new ToolType("Shvl", 0);
    public static ToolType hoe = new ToolType("Hoe", 1);
    public static ToolType sword = new ToolType("Swrd", 2);
    public static ToolType pickaxe = new ToolType("Pick", 3);
    public static ToolType axe = new ToolType("Axe", 4);

    public final String name;
    public final int sprite;

    private ToolType(String name, int sprite) {
        this.name = name;
        this.sprite = sprite;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ToolType)
                && ((ToolType) obj).name.equals(this.name);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.name);
        return hash;
    }

}
