package me.catalysmrl.catamines.mine.components.composition.drop;

import me.catalysmrl.catamines.api.serialization.DeserializationException;
import me.catalysmrl.catamines.api.serialization.SectionSerializable;
import me.catalysmrl.catamines.mine.reward.Rewardable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class CataMineItem implements Rewardable, SectionSerializable {

    private ItemStack item;
    private double chance;
    private boolean fortune;

    public CataMineItem(ItemStack item) {
        this.item = item;
    }

    @Override
    public void serialize(ConfigurationSection section) {
        section.set("item", item.serialize());
        section.set("chance", chance);
        section.set("fortune", fortune);
    }

    public static CataMineItem deserialize(ConfigurationSection section) throws DeserializationException {
        ItemStack item = section.getItemStack("item");
        if (item == null) throw new DeserializationException("Could not deserialize item");

        double chance = section.getDouble("chance", 0d);
        boolean fortune = section.getBoolean("fortune", false);

        CataMineItem mineItem = new CataMineItem(item);
        mineItem.setChance(chance);
        mineItem.setFortune(fortune);

        return mineItem;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public boolean isFortune() {
        return fortune;
    }

    public void setFortune(boolean fortune) {
        this.fortune = fortune;
    }
}
