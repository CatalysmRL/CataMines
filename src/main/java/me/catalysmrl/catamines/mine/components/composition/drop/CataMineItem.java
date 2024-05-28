package me.catalysmrl.catamines.mine.components.composition.drop;

import me.catalysmrl.catamines.api.serialization.DeserializationException;
import me.catalysmrl.catamines.api.serialization.SectionSerializable;
import me.catalysmrl.catamines.mine.reward.Rewardable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class CataMineItem implements Rewardable, SectionSerializable {

    double chance;
    boolean fortune;

    ItemStack item;

    @Override
    public void serialize(ConfigurationSection section) {
        section.set("chance", chance);
        section.set("fortune", fortune);
        section.set("item", item.serialize());
    }

    public static CataMineItem deserialize(ConfigurationSection section) throws DeserializationException {
        double chance = section.getDouble("chance", 0d);
        boolean fortune = section.getBoolean("fortune", false);

        ItemStack item = section.getItemStack("item");
        if (item == null) throw new DeserializationException("Could not deserialize item");
        return null;
    }

}
