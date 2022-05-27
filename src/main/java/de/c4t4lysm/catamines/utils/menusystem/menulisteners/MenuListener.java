package de.c4t4lysm.catamines.utils.menusystem.menulisteners;

import de.c4t4lysm.catamines.utils.menusystem.Menu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {

        InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof Menu) {

            Menu menu = (Menu) holder;

            menu.handleMenu(event);
        }

    }


}
