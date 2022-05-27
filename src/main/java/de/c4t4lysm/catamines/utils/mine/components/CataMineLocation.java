package de.c4t4lysm.catamines.utils.mine.components;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class CataMineLocation implements ConfigurationSerializable {
    private final double x;
    private final double y;
    private final double z;

    public CataMineLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CataMineLocation(Location location) {
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockX();
    }

    public static CataMineLocation deserialize(Map<String, Object> serializedMap) {
        double x = (double) serializedMap.get("x");
        double y = (double) serializedMap.get("y");
        double z = (double) serializedMap.get("z");
        return new CataMineLocation(x, y, z);
    }

    public Location toLocation(World world) {
        return new Location(world, x, y, z, 0, 0);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serializedLocation = new LinkedHashMap<>();
        serializedLocation.put("x", x);
        serializedLocation.put("y", y);
        serializedLocation.put("z", z);
        return serializedLocation;
    }
}
