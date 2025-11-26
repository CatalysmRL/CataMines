package me.catalysmrl.catamines.mine.components.region.impl;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.world.World;
import me.catalysmrl.catamines.api.serialization.DeserializationException;
import me.catalysmrl.catamines.mine.components.region.AbstractCataMineRegion;
import me.catalysmrl.catamines.utils.worldedit.VectorParser;
import me.catalysmrl.catamines.utils.worldedit.WorldEditUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class SchematicRegion extends AbstractCataMineRegion {

    private String schematicName;
    private World world;
    private BlockVector3 location;
    private Clipboard clipboard;

    public SchematicRegion(String name, String schematicName, RegionSelector selector) {
        super(name);
        define(schematicName, selector);
    }

    public SchematicRegion(String name, String schematicName, World world, BlockVector3 location) {
        super(name);
        define(schematicName, world, location);
    }

    public void define(String schematicName, World world, BlockVector3 location) {
        this.schematicName = schematicName;
        this.world = world;
        this.location = location;
        this.clipboard = WorldEditUtils.loadSchematic(schematicName);
    }

    public void define(String schematicName, RegionSelector selector) {
        define(schematicName, selector.getWorld(), selector.getPrimaryPosition());
    }

    @Override
    public void redefineRegion(RegionSelector selector) {
        this.world = selector.getWorld();
        this.location = selector.getPrimaryPosition();
    }

    @Override
    public void fill() {
        WorldEditUtils.pasteSchematic(clipboard, world, location);
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
        super.serialize(section);
        section.set("schematic-name", schematicName);
        section.set("world", world.getName());
        section.set("location", location.toParserString());
        serializeCompositions(section.createSection("compositions"));
    }

    public static SchematicRegion deserialize(ConfigurationSection section) throws DeserializationException {

        String name = section.getString("name");
        if (name == null)
            throw new DeserializationException("No name specified");

        double chance = section.getDouble("chance", 0d);

        String schematicName = section.getString("schematic-name");
        if (schematicName == null)
            throw new DeserializationException("No schematic specified");

        String worldName = section.getString("world");
        if (worldName == null)
            throw new DeserializationException("No world specified");

        org.bukkit.World bukkitWorld = Bukkit.getWorld(worldName);
        if (bukkitWorld == null)
            throw new DeserializationException("World " + worldName + " not found");

        World worldEditWorld = BukkitAdapter.adapt(bukkitWorld);

        BlockVector3 location;
        try {
            location = VectorParser.asBlockVector3(section.getString("location"));
        } catch (IllegalArgumentException exception) {
            throw new DeserializationException("Invalid location", exception);
        }

        SchematicRegion schematicRegion = new SchematicRegion(name, schematicName, worldEditWorld, location);
        schematicRegion.setChance(chance);

        return schematicRegion;
    }

    @Override
    public String toString() {
        return "SchematicRegion{" +
                "schemName='" + schematicName + '\'' +
                ", location=" + location +
                ", clipboard=" + clipboard +
                "} " + super.toString();
    }
}
