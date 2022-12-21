package me.catalysmrl.catamines.managers.blockmanagers;

import me.catalysmrl.catamines.mine.abstraction.region.CataMineRegion;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public abstract class BlockApplicator {

    private static final long MAX_BLOCK_UPDATES_PER_TICK = 5_000_000L;

    protected JavaPlugin plugin;
    protected Deque<CataMineRegion> resetQueue = new ArrayDeque<>();

    public BlockApplicator(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract void start();

    public abstract void cancel();

    public void queueForReset(CataMineRegion region) {
        resetQueue.offer(region);
    }

    protected void handleQueue() {
        long blockUpdates = 0L;
        Iterator<CataMineRegion> iterator = resetQueue.iterator();

        while (iterator.hasNext()) {
            CataMineRegion region = iterator.next();
            iterator.remove();
            blockUpdates += region.getVolume();
            region.fill();

            if (blockUpdates > MAX_BLOCK_UPDATES_PER_TICK) {
                break;
            }
        }
    }
}
