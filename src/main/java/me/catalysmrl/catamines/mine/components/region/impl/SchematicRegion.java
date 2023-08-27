package me.catalysmrl.catamines.mine.components.region.impl;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import me.catalysmrl.catamines.mine.components.region.AbstractCataMineRegion;
import me.catalysmrl.catamines.utils.worldedit.WorldEditUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

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
    public @NotNull Map<String, Object> serialize() {
        return null;
    }

    /**
     * Used for deserialization of a ConfigurationSerializable class.
     * Returns an instance of a SchematicRegion constructed from a map
     * provided by {@link org.bukkit.configuration.serialization.ConfigurationSerialization}.
     *
     * @param serializedRegion the serialized object as a Map
     * @return a new SchematicRegion instance constructed from the map
     */
    public static SelectionRegion deserialize(Map<String, Object> serializedRegion) {
        return null;
    }
}
