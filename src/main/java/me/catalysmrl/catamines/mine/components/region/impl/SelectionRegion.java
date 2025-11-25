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

import java.util.List;

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
        getCompositionManager().getUpcoming().ifPresent(composition -> {
            if (composition.canReset())
                WorldEditUtils.pasteRegion(region, composition.getRandomPattern());
        });
        getCompositionManager().next();
    }

    public void defineRegion(RegionSelector selector) throws IncompleteRegionException {
        this.selectionType = SelectionType.getType(selector.getTypeName());
        this.region = selector.getRegion().clone();
    }

    @Override
    public void redefineRegion(RegionSelector selector) {
        defineRegion(selector);
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
        super.serialize(section);
        section.set("world", region.getWorld() == null ? "null" : region.getWorld().getName());
        section.set("selection-type", selectionType.toString());
        serializeRegion(section.createSection("selection"));
        serializeCompositions(section.createSection("compositions"));
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
                List<String> vertices = convexPolyhedralRegion.getVertices().stream().map(BlockVector3::toParserString)
                        .toList();
                section.set("vertices", vertices);
            }
            default -> section.set("parameters", "undefined");
        }
    }

    public static SelectionRegion deserialize(ConfigurationSection section) throws DeserializationException {

        String name = section.getString("name");
        if (name == null)
            throw new DeserializationException("No name specified");

        double chance = section.getDouble("chance", 0d);

        String worldName = section.getString("world");
        if (worldName == null)
            throw new DeserializationException("No world specified in region " + name);

        org.bukkit.World bukkitWorld = Bukkit.getWorld(worldName);
        if (bukkitWorld == null)
            throw new DeserializationException("World " + worldName + " not found in region " + name);

        World worldEditWorld = BukkitAdapter.adapt(bukkitWorld);

        SelectionType selectionType = SelectionType.valueOf(section.getString("selection-type"));
        if (selectionType == SelectionType.NONE)
            throw new DeserializationException("Invalid selection type in region " + name);

        ConfigurationSection selectionSection = section.getConfigurationSection("selection");
        if (selectionSection == null)
            throw new DeserializationException();

        Region region = new NullRegion();
        try {
            switch (selectionType) {
                case CUBOID -> region = new CuboidRegion(worldEditWorld,
                        VectorParser.asBlockVector3(selectionSection.getString("min")),
                        VectorParser.asBlockVector3(selectionSection.getString("max")));
                case CYLINDER -> region = new CylinderRegion(worldEditWorld,
                        VectorParser.asBlockVector3(selectionSection.getString("center")),
                        VectorParser.asVector2(selectionSection.getString("radius")),
                        selectionSection.getInt("minY"),
                        selectionSection.getInt("maxY"));
                case ELLIPSOID, SPHERE -> region = new EllipsoidRegion(worldEditWorld,
                        VectorParser.asBlockVector3(selectionSection.getString("center")),
                        VectorParser.asVector3(selectionSection.getString("radius")));
                case POLYGONAL2D -> {
                    List<BlockVector2> points = selectionSection.getStringList("points").stream()
                            .map(VectorParser::asBlockVector2).toList();
                    region = new Polygonal2DRegion(worldEditWorld, points, selectionSection.getInt("minY"),
                            selectionSection.getInt("maxY"));
                }
                case CONVEXPOLYHEDRAL -> {
                    List<BlockVector3> vertices = selectionSection.getStringList("vertices").stream()
                            .map(VectorParser::asBlockVector3).toList();
                    ConvexPolyhedralRegion convexRegion = new ConvexPolyhedralRegion(worldEditWorld);

                    for (BlockVector3 blockVector3 : vertices) {
                        convexRegion.addVertex(blockVector3);
                    }

                    region = convexRegion;
                }
                default -> throw new IllegalArgumentException("Unexpected value: " + selectionType);
            }
        } catch (IllegalArgumentException exception) {
            throw new DeserializationException("Could not deserialize region " + region, exception);
        }

        SelectionRegion selectionRegion = new SelectionRegion(name, selectionType, region);
        selectionRegion.setChance(chance);

        ConfigurationSection compositionsSection = section.getConfigurationSection("compositions");
        if (compositionsSection != null) {
            deserializeCompositions(compositionsSection)
                    .forEach(composition -> selectionRegion.getCompositionManager().add(composition));
        }

        return selectionRegion;
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
