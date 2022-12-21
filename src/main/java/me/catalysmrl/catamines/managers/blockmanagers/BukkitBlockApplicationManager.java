package me.catalysmrl.catamines.managers.blockmanagers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class BukkitBlockApplicationManager extends BlockApplicator {

    private BukkitTask bukkitTask;

    public BukkitBlockApplicationManager(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void start() {
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::handleQueue, 0L, 1L);
    }

    @Override
    public void cancel() {
        bukkitTask.cancel();
    }
}
