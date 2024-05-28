package me.catalysmrl.catamines.mine.components.composition.drop;

import me.catalysmrl.catamines.api.serialization.SectionSerializable;
import me.catalysmrl.catamines.mine.reward.CataMineReward;
import me.catalysmrl.catamines.mine.reward.Rewardable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

public class CataMineItem implements Rewardable, SectionSerializable {

    double chance;
    CataMineReward reward;

    ItemStack item;
    boolean fortune;

    @Override
    public void serialize(ConfigurationSection section) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("chance", chance);
        result.put("reward", reward);
        result.put("item", item.serialize());
        result.put("fortune", fortune);
    }

    public static CataMineItem deserialize(ConfigurationSection section) {
        return null;
    }

}
