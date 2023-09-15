package me.catalysmrl.catamines.mine.components.region.impl;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.*;
import com.sk89q.worldedit.world.World;
import me.catalysmrl.catamines.mine.components.region.AbstractCataMineRegion;
import me.catalysmrl.catamines.utils.worldedit.VectorParser;
import me.catalysmrl.catamines.utils.worldedit.WorldEditUtils;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        WorldEditUtils.pasteRegion(region, getCompositions().get(0).getRandomPattern());
    }

    public void defineRegion(RegionSelector selector) throws IncompleteRegionException {
        this.selectionType = SelectionType.getType(selector.getTypeName());
        this.region = selector.getRegion();
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
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> serializedRegion = new LinkedHashMap<>();
        serializedRegion.put("name", name);
        serializedRegion.put("selection_type", selectionType);
        serializedRegion.put("world", region.getWorld() == null ? "null" : region.getWorld().getName());
        serializedRegion.put("region", serializeRegion());
        return serializedRegion;
    }

    public Map<String, Object> serializeRegion() {
        Map<String, Object> mappedRegion = new LinkedHashMap<>();
        switch (selectionType) {
            case CUBOID -> {
                mappedRegion.put("min", region.getMinimumPoint().toParserString());
                mappedRegion.put("max", region.getMaximumPoint().toParserString());
            }
            case CYLINDER -> {
                CylinderRegion cylinderRegion = (CylinderRegion) region;
                mappedRegion.put("center", cylinderRegion.getCenter().toParserString());
                mappedRegion.put("radius", cylinderRegion.getRadius().toParserString());
                mappedRegion.put("minY", cylinderRegion.getMinimumY());
                mappedRegion.put("maxY", cylinderRegion.getMaximumY());
            }
            case ELLIPSOID, SPHERE -> {
                EllipsoidRegion ellipsoidRegion = (EllipsoidRegion) region;
                mappedRegion.put("center", ellipsoidRegion.getCenter().toParserString());
                mappedRegion.put("radius", ellipsoidRegion.getRadius().toParserString());
            }
            case POLYGONAL2D -> {
                Polygonal2DRegion polygonal2DRegion = (Polygonal2DRegion) region;
                List<String> points = polygonal2DRegion.getPoints().stream().map(BlockVector2::toParserString).toList();
                mappedRegion.put("points", points);

                mappedRegion.put("minY", polygonal2DRegion.getMinimumY());
                mappedRegion.put("maxY", polygonal2DRegion.getMinimumY());
            }
            case CONVEXPOLYHEDRAL -> {
                ConvexPolyhedralRegion convexPolyhedralRegion = (ConvexPolyhedralRegion) region;
                List<String> vertices = convexPolyhedralRegion.getVertices().stream().map(BlockVector3::toParserString).toList();
                mappedRegion.put("vertices", vertices);
            }
            default -> mappedRegion.put("undefined", "undefined");
        }

        return mappedRegion;
    }

    /**
     * Used for deserialization of a ConfigurationSerializable class.
     * Returns an instance of a SelectionRegion constructed from a map
     * provided by {@link org.bukkit.configuration.serialization.ConfigurationSerialization}.
     *
     * @param serializedRegion the serialized object as a Map
     * @return a new SelectionRegion instance constructed from the map
     */
    public static SelectionRegion deserialize(Map<String, Object> serializedRegion) {
        String name = (String) serializedRegion.get("name");
        SelectionType selectionType = SelectionType.valueOf((String) serializedRegion.get("selection_type"));
        World world = BukkitAdapter.adapt(Bukkit.getWorld((String) serializedRegion.get("world")));

        Region region = new NullRegion();
        switch (selectionType) {
            case CUBOID -> region = new CuboidRegion(world,
                    VectorParser.asBlockVector3((String) serializedRegion.get("min")),
                    VectorParser.asBlockVector3((String) serializedRegion.get("min")));
            case CYLINDER, SPHERE -> region = new CylinderRegion(world,
                    VectorParser.asBlockVector3((String) serializedRegion.get("center")),
                    VectorParser.asVector2((String) serializedRegion.get("radius")),
                    (int) serializedRegion.get("minY"),
                    (int) serializedRegion.get("maxY"));

        }

        return new SelectionRegion(name, selectionType, region);
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
