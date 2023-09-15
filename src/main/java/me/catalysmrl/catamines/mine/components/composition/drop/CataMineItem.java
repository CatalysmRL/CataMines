package me.catalysmrl.catamines.mine.components.composition.drop;

import me.catalysmrl.catamines.mine.components.reward.CataMineReward;
import me.catalysmrl.catamines.mine.components.reward.Rewardable;
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
    public @NotNull Map<String, Object> serialize() {
        return null;
    }

    public static CataMineItem deserialize(Map<String, Object> serializedItem) {
        return null;
    }
}
