package me.catalysmrl.legacycatamines.menusystem.menulisteners;

import me.catalysmrl.legacycatamines.menusystem.Menu;
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
