package me.catalysmrl.catamines.mine.reward;

import me.catalysmrl.catamines.api.mine.CataMine;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MineReward extends CataMineReward {

    public void execute(CataMine mine) {
        executeCommands(Bukkit.getConsoleSender());
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return null;
    }
}
