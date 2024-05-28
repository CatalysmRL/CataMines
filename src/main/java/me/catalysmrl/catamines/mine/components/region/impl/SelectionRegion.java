package me.catalysmrl.catamines.mine.components.region.impl;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.*;
import com.sk89q.worldedit.world.World;
import me.catalysmrl.catamines.api.serialization.DeserializationException;
import me.catalysmrl.catamines.mine.components.region.AbstractCataMineRegion;
import me.catalysmrl.catamines.utils.worldedit.VectorParser;
import me.catalysmrl.catamines.utils.worldedit.WorldEditUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.List;

@SerializableAs("SelectionRegion")
public class SelectionRegion extends AbstractCataMineRegion {

    private SelectionType selectionType;
    private Region region;

    /**
     * Creates a CataMineRegion wrapping a WorldEdit Region.
     * On creation of a mine the selection of the player will
     * be taken.
     *
     * @param selector WorldEdit region selector
     * @throws IncompleteRegionException When the region is not fully defined
     */
    public SelectionRegion(String name, RegionSelector selector) {
        super(name);
        defineRegion(selector);
    }

    public SelectionRegion(String name, SelectionType selectionType, Region region) {
        super(name);
        this.selectionType = selectionType;
        this.region = region;
    }

    @Override
    public void fill() {
        Bukkit.broadcastMessage("Filling SelectionRegion");
        getCompositionManager().getUpcoming().ifPresent(composition -> WorldEditUtils.pasteRegion(region, composition.getRandomPattern()));
        getCompositionManager().next();
    }

    public void defineRegion(RegionSelector selector) throws IncompleteRegionException {
        this.selectionType = SelectionType.getType(selector.getTypeName());
        this.region = selector.getRegion().clone();
    }

    @Override
    public RegionType getType() {
        return RegionType.SELECTION;
    }

    @Override
    public long getVolume() {
        return region.getVolume();
    }

    @Override
    public void serialize(ConfigurationSection section) {
        section.set("name", name);
        section.set("selection_type", selectionType.toString());
        section.set("world", region.getWorld() == null ? "null" : region.getWorld().getName());
        serializeRegion(section.createSection("region"));
    }

    public void serializeRegion(ConfigurationSection section) {
        switch (selectionType) {
            case CUBOID -> {
                section.set("min", region.getMinimumPoint().toParserString());
                section.set("max", region.getMaximumPoint().toParserString());
            }
            case CYLINDER -> {
                CylinderRegion cylinderRegion = (CylinderRegion) region;
                section.set("center", cylinderRegion.getCenter().toParserString());
                section.set("radius", cylinderRegion.getRadius().toParserString());
                section.set("minY", cylinderRegion.getMinimumY());
                section.set("maxY", cylinderRegion.getMaximumY());
            }
            case ELLIPSOID, SPHERE -> {
                EllipsoidRegion ellipsoidRegion = (EllipsoidRegion) region;
                section.set("center", ellipsoidRegion.getCenter().toParserString());
                section.set("radius", ellipsoidRegion.getRadius().toParserString());
            }
            case POLYGONAL2D -> {
                Polygonal2DRegion polygonal2DRegion = (Polygonal2DRegion) region;
                List<String> points = polygonal2DRegion.getPoints().stream().map(BlockVector2::toParserString).toList();
                section.set("points", points);

                section.set("minY", polygonal2DRegion.getMinimumY());
                section.set("maxY", polygonal2DRegion.getMinimumY());
            }
            case CONVEXPOLYHEDRAL -> {
                ConvexPolyhedralRegion convexPolyhedralRegion = (ConvexPolyhedralRegion) region;
                List<String> vertices = convexPolyhedralRegion.getVertices().stream().map(BlockVector3::toParserString).toList();
                section.set("vertices", vertices);
            }
            default -> section.set("undefined", "undefined");
        }
    }

    public static SelectionRegion deserialize(ConfigurationSection section) throws DeserializationException {

        String name = section.getString("name");
        if (name == null) throw new DeserializationException("No name specified");

        SelectionType selectionType = SelectionType.valueOf(section.getString("selection_type"));
        if (selectionType == SelectionType.NONE)
            throw new DeserializationException("Invalid selection type in region " + name);

        String worldName = section.getString("world");
        if (worldName == null) throw new DeserializationException("No world specified in region " + name);

        org.bukkit.World bukkitWorld = Bukkit.getWorld(worldName);
        if (bukkitWorld == null)
            throw new DeserializationException("World " + worldName + " not found in region " + name);

        World worldEditWorld = BukkitAdapter.adapt(bukkitWorld);

        Region region = new NullRegion();
        try {
            switch (selectionType) {
                case CUBOID -> region = new CuboidRegion(worldEditWorld,
                        VectorParser.asBlockVector3(section.getString("min")),
                        VectorParser.asBlockVector3(section.getString("max")));
                case CYLINDER -> region = new CylinderRegion(worldEditWorld,
                        VectorParser.asBlockVector3(section.getString("center")),
                        VectorParser.asVector2(section.getString("radius")),
                        section.getInt("minY"),
                        section.getInt("maxY"));
                case ELLIPSOID, SPHERE -> region = new EllipsoidRegion(worldEditWorld,
                        VectorParser.asBlockVector3(section.getString("center")),
                        VectorParser.asVector3(section.getString("radius")));
                case POLYGONAL2D -> {
                    List<BlockVector2> points = section.getStringList("points").stream().map(VectorParser::asBlockVector2).toList();
                    region = new Polygonal2DRegion(worldEditWorld, points, section.getInt("minY"), section.getInt("maxY"));
                }
                case CONVEXPOLYHEDRAL -> {
                    List<BlockVector3> vertices = section.getStringList("vertices").stream().map(VectorParser::asBlockVector3).toList();
                    ConvexPolyhedralRegion convexRegion = new ConvexPolyhedralRegion(worldEditWorld);

                    for (BlockVector3 blockVector3 : vertices) {
                        convexRegion.addVertex(blockVector3);
                    }

                    region = convexRegion;
                }
            }
        } catch (IllegalArgumentException exception) {
            throw new DeserializationException("Could not deserialize region " + region, exception);
        }

        return new SelectionRegion(name, selectionType, region);
    }

    @Override
    public String toString() {
        return "SelectionRegion{" +
                "selectionType=" + selectionType +
                ", region=" + region +
                "} " + super.toString();
    }

    public enum SelectionType {

        NONE("none"),
        CUBOID("cuboid"),
        CYLINDER("Cylinder"),
        ELLIPSOID("ellipsoid"),
        SPHERE("sphere"),
        POLYGONAL2D("2Dx1D polygon"),
        CONVEXPOLYHEDRAL("Convex Polyhedron");

        private final String typeName;

        SelectionType(String typeName) {
            this.typeName = typeName;
        }

        public static SelectionType getType(String typeName) {
            for (SelectionType type : values()) {
                if (type.typeName.equalsIgnoreCase(typeName)) {
                    return type;
                }
            }

            return NONE;
        }

    }
}
