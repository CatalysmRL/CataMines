package de.c4t4lysm.catamines.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ItemStackBuilder {

    public static ItemStack buildItem(Material material, final String name, final String... lore) {

        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        if (meta != null) {

            // Set the name of the item
            meta.setDisplayName(name);

            // Set the lore of the item
            meta.setLore(Arrays.asList(lore));

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack buildItem(Material material, final String name, final List<String> lore) {

        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        if (meta != null) {

            // Set the name of the item
            meta.setDisplayName(name);

            // Set the lore of the item
            meta.setLore(lore);

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack buildItem(Material material, int amount, final String name, final List<String> lore) {

        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();

        if (meta != null) {

            // Set the name of the item
            meta.setDisplayName(name);

            // Set the lore of the item
            meta.setLore(lore);

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack buildItem(Material material, int amount, final String name, final String... lore) {

        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();

        if (meta != null) {

            // Set the name of the item
            meta.setDisplayName(name);

            // Set the lore of the item
            meta.setLore(Arrays.asList(lore));

            item.setItemMeta(meta);
        }

        return item;
    }

}
