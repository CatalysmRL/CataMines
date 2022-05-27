package de.c4t4lysm.catamines.utils.menusystem.menus.minemenus;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.utils.ItemStackBuilder;
import de.c4t4lysm.catamines.utils.menusystem.Menu;
import de.c4t4lysm.catamines.utils.menusystem.PlayerMenuUtility;
import de.c4t4lysm.catamines.utils.menusystem.menus.MineListMenu;
import de.c4t4lysm.catamines.utils.menusystem.menus.MineMenu;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class DeleteConfirmMenu extends Menu {

    private final CuboidCataMine cuboidCataMine;

    public DeleteConfirmMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        this.cuboidCataMine = playerMenuUtility.getMine();
    }

    @Override
    public String getMenuName() {
        return ChatColor.DARK_RED + "Delete Mine?";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || Objects.equals(event.getClickedInventory(), event.getWhoClicked().getInventory())) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        switch (event.getRawSlot()) {
            case 0:
                CataMines.removePlayerMenuUtility(player);
                player.performCommand("cm delete " + cuboidCataMine.getName());
                new MineListMenu(CataMines.getPlayerMenuUtility(player)).open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                updateMenus();
                break;
            case 8:
                new MineMenu(playerMenuUtility, playerMenuUtility.getMine()).open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                break;
        }
    }

    @Override
    public void setMenuItems() {

        inventory.setItem(0, ItemStackBuilder.buildItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.GREEN + "" + ChatColor.BOLD + "CONFIRM", "", ChatColor.RED + "Permanently deletes this mine"));
        inventory.setItem(8, ItemStackBuilder.buildItem(Material.RED_STAINED_GLASS_PANE, ChatColor.DARK_RED + "" + ChatColor.BOLD + "CANCEL", "", ChatColor.GREEN + "Cancels the process"));

        setFillerGlass();
    }
}
