package de.c4t4lysm.catamines.utils.mine.components;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;

public class CataMineCuboidRegion implements Iterable, Cloneable {
    private final World world;
    private final Location minPoint;
    private final Location maxPoint;

    public CataMineCuboidRegion(Location loc1, Location loc2) {
        this.world = null;
        this.minPoint = calcMinPoint(loc1, loc2);
        this.maxPoint = calcMaxPoint(loc1, loc2);
    }

    public CataMineCuboidRegion(World world, Location loc1, Location loc2) {
        this.world = world;
        this.minPoint = calcMinPoint(loc1, loc2);
        this.maxPoint = calcMaxPoint(loc1, loc2);
    }

    public Location calcMinPoint(Location loc1, Location loc2) {
        if (!Objects.equals(loc1.getWorld(), loc2.getWorld()))
            throw new IllegalArgumentException("Points must be in the same world");
        double minX = Math.min(loc1.getX(), loc2.getX());
        double minY = Math.min(loc1.getY(), loc2.getY());
        double minZ = Math.min(loc1.getZ(), loc2.getZ());

        return new Location(loc1.getWorld(), minX, minY, minZ);
    }

    public Location calcMaxPoint(Location loc1, Location loc2) {
        if (!Objects.equals(loc1.getWorld(), loc2.getWorld()))
            throw new IllegalArgumentException("Points must be in the same world");
        double maxX = Math.max(loc1.getX(), loc2.getX());
        double maxY = Math.max(loc1.getY(), loc2.getY());
        double maxZ = Math.max(loc1.getZ(), loc2.getZ());

        return new Location(loc1.getWorld(), maxX, maxY, maxZ);
    }

    public Location getMinPoint() {
        return minPoint;
    }

    public Location getMaxPoint() {
        return maxPoint;
    }

    @NotNull
    @Override
    public Iterator<Location> iterator() {
        return new Iterator() {
            final int minX = minPoint.getBlockX();
            final int minY = minPoint.getBlockY();
            final int minZ = minPoint.getBlockZ();

            final int maxX = maxPoint.getBlockX();
            final int maxY = maxPoint.getBlockY();
            final int maxZ = maxPoint.getBlockZ();

            final int x = minPoint.getBlockX();
            final int y = minPoint.getBlockY();
            final int z = minPoint.getBlockZ();

            boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Location next() {
                // TODO: Calculate the block to return for iteration
                return null;
            }
        };
    }

    @Override
    public CataMineCuboidRegion clone() {
        try {
            return (CataMineCuboidRegion) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
