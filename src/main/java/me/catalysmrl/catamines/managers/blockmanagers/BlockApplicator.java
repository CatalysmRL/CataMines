package me.catalysmrl.catamines.managers.blockmanagers;

import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayDeque;
import java.util.Deque;

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

        while (!resetQueue.isEmpty()) {
            CataMineRegion region = resetQueue.pop();
            blockUpdates += region.getVolume();
            region.fill();

            if (blockUpdates > MAX_BLOCK_UPDATES_PER_TICK) break;
        }
    }
}
