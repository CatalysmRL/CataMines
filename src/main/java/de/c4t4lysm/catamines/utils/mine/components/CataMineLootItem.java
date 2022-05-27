package de.c4t4lysm.catamines.utils.mine.components;

import de.c4t4lysm.catamines.CataMines;
import org.bukkit.inventory.ItemStack;

public class CataMineLootItem {

    private ItemStack item;
    private double chance;

    public CataMineLootItem(ItemStack item) {
        this.item = item;
        this.chance = 100d;
    }

    public CataMineLootItem(ItemStack item, double chance) {
        this.item = item;
        if (chance < 0 || chance > 100) {
            throw new IllegalArgumentException(CataMines.getInstance().getLangString("Error-Messages.Mine.Invalid-Chance"));
        }
        this.chance = Math.round(chance * 1000) / 1000d;
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
        if (chance < 0 || chance > 100) {
            throw new IllegalArgumentException(CataMines.getInstance().getLangString("Error-Messages.Mine.Invalid-Chance"));
        }
        this.chance = Math.round(chance * 1000) / 1000d;
    }
}
