package de.c4t4lysm.catamines.utils.mine.components;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

public class CataMineApplyBlockManager {

    private static CataMineApplyBlockManager instance;

    public static CataMineApplyBlockManager getInstance() {
        if (instance == null) {
            instance = new CataMineApplyBlockManager();
        }

        return instance;
    }

    public void setBlocks(CataMineCuboidRegion region, CataMineRandomPattern randomPattern) {
        Location minP = region.getMinPoint();
        Location maxP = region.getMaxPoint();
        World world = Objects.equals(minP.getWorld(), maxP.getWorld()) ? minP.getWorld() : null;

    }


}
