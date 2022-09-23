package de.c4t4lysm.catamines.listeners;

import com.sk89q.worldedit.math.BlockVector3;
import de.c4t4lysm.catamines.schedulers.MineManager;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        //TODO: Might want to revert to checking the gamemode
        if (event.isCancelled() || MineManager.getInstance().tasksStopped()) {
            return;
        }

        Location blockLocation = event.getBlock().getLocation();

        for (CuboidCataMine mine : MineManager.getInstance().getMines()) {
            if (mine.isStopped() || mine.getRegion() == null) {
                continue;
            }
            if (blockLocation.getWorld().getName().equals(mine.getWorld())
                    && mine.getRegion().contains(BlockVector3.at(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ()))) {
                mine.handleBlockBreak(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlace(BlockPlaceEvent event) {
        if (event.isCancelled() || MineManager.getInstance().tasksStopped()) {
            return;
        }

        Location blockLocation = event.getBlock().getLocation();

        for (CuboidCataMine mine : MineManager.getInstance().getMines()) {
            if (mine.isStopped() || mine.getRegion() == null) {
                continue;
            }
            if (blockLocation.getWorld().getName().equals(mine.getWorld())
                    && mine.getRegion().contains(BlockVector3.at(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ()))) {
                mine.setBlockCount(mine.getBlockCount() + 1);
            }
        }
    }


}
