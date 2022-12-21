package me.catalysmrl.catamines.managers.blockmanagers;

import com.fastasyncworldedit.core.FaweAPI;
import org.bukkit.plugin.java.JavaPlugin;

public class FastAsyncBlockApplicationManager extends BlockApplicator {

    private int taskID;

    public FastAsyncBlockApplicationManager(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void start() {
        taskID = FaweAPI.getTaskManager().repeatAsync(this::handleQueue, 1);
    }

    @Override
    public void cancel() {
        FaweAPI.getTaskManager().cancel(taskID);
    }
}
