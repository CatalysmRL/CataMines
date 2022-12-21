package me.catalysmrl.catamines.mine.abstraction.reward.rewards;

import me.catalysmrl.catamines.mine.abstraction.CataMine;
import me.catalysmrl.catamines.mine.abstraction.reward.CataMineReward;
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
