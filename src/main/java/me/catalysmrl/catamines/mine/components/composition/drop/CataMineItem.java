package me.catalysmrl.catamines.mine.components.composition.drop;

import me.catalysmrl.catamines.mine.abstraction.reward.CataMineReward;
import me.catalysmrl.catamines.mine.abstraction.reward.Rewardable;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CataMineItem implements Rewardable, ConfigurationSerializable {

    double chance;
    CataMineReward reward;

    ItemStack item;
    boolean fortune;

    @Override
    public double getChance() {
        return chance;
    }

    @Override
    public void setChance(double chance) {
        this.chance = chance;
    }

    @Override
    public CataMineReward getReward() {
        return reward;
    }

    @Override
    public void setReward(CataMineReward reward) {
        this.reward = reward;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return null;
    }

    public static CataMineItem deserialize(Map<String, Object> serItem) {
        return null;
    }
}
