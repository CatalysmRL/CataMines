package me.catalysmrl.catamines.gui.impl;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public abstract class BaseGui implements InventoryHolder {

    private Inventory inventory;

    public abstract String getName();

    public abstract int getSize();

    public abstract void setItems();

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
