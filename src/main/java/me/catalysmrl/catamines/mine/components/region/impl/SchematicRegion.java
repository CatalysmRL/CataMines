package me.catalysmrl.catamines.mine.components.region.impl;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import me.catalysmrl.catamines.mine.components.region.AbstractCataMineRegion;
import me.catalysmrl.catamines.utils.worldedit.WorldEditUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("SchematicRegion")
public class SchematicRegion extends AbstractCataMineRegion {

    String schemName;
    BlockVector3 location;
    Clipboard clipboard;

    public SchematicRegion(String name, String schemName, BlockVector3 location) {
        super(name);
        this.schemName = schemName;
        this.location = location;
        this.clipboard = WorldEditUtils.loadSchematic(schemName);
    }

    @Override
    public void fill() {
        WorldEditUtils.pasteSchematic(clipboard, location);
    }

    @Override
    public RegionType getType() {
        return RegionType.SCHEMATIC;
    }

    @Override
    public long getVolume() {
        return clipboard.getVolume();
    }

    @Override
    public void serialize(ConfigurationSection section) {

    }

    public static SelectionRegion deserialize(ConfigurationSection section) {
        return null;
    }

    @Override
    public String toString() {
        return "SchematicRegion{" +
                "schemName='" + schemName + '\'' +
                ", location=" + location +
                ", clipboard=" + clipboard +
                "} " + super.toString();
    }
}
