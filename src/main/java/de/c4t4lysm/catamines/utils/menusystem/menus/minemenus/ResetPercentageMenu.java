package de.c4t4lysm.catamines.utils.menusystem.menus.minemenus;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.utils.ItemStackBuilder;
import de.c4t4lysm.catamines.utils.menusystem.Menu;
import de.c4t4lysm.catamines.utils.menusystem.PlayerMenuUtility;
import de.c4t4lysm.catamines.utils.menusystem.menus.MineMenu;
import de.c4t4lysm.catamines.utils.mine.components.CataMineResetMode;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class ResetPercentageMenu extends Menu {

    private final CataMines plugin;
    private final CuboidCataMine mine;

    public ResetPercentageMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        playerMenuUtility.setMenu(this);
        this.plugin = CataMines.getInstance();
        this.mine = playerMenuUtility.getMine();
    }

    @Override
    public String getMenuName() {
        return plugin.getLangString("GUI.Reset-Percentage-Menu.Title").replaceAll("%percentage%", String.valueOf(mine.getResetPercentage()));
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);
        if (event.getCurrentItem() == null || Objects.equals(event.getClickedInventory(), event.getWhoClicked().getInventory())) {
            return;
        }

        double addResetPercentage = 0;
        switch (event.getRawSlot()) {
            case 45:
                new MineMenu(playerMenuUtility).open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                return;
            case 6:
                new ResetDelayMenu(playerMenuUtility).open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                return;
            case 3 * 9 - 8:
                addResetPercentage = 0.01;
                break;
            case 3 * 9 - 7:
            case 5 * 9 - 8:
                addResetPercentage = 0.1;
                break;
            case 3 * 9 - 6:
            case 5 * 9 - 7:
                addResetPercentage = 1;
                break;
            case 4 * 9 - 8:
                addResetPercentage = 0.05;
                break;
            case 4 * 9 - 7:
                addResetPercentage = 0.5;
                break;
            case 4 * 9 - 6:
                addResetPercentage = 5;
                break;
            case 5 * 9 - 6:
                addResetPercentage = 10;
                break;
            case 3 * 9 - 4:
            case 5 * 9 - 3:
                addResetPercentage = -1;
                break;
            case 3 * 9 - 3:
            case 5 * 9 - 2:
                addResetPercentage = -0.1;
                break;
            case 3 * 9 - 2:
                addResetPercentage = -0.01;
                break;
            case 4 * 9 - 4:
                addResetPercentage = -5;
                break;
            case 4 * 9 - 3:
                addResetPercentage = -0.5;
                break;
            case 4 * 9 - 2:
                addResetPercentage = -0.05;
                break;
            case 5 * 9 - 4:
                addResetPercentage = -10;
                break;
        }

        if (addResetPercentage == 0) {
            return;
        }

        mine.setResetPercentage(mine.getResetPercentage() + addResetPercentage);
        mine.save();
        updateMenus();

    }

    @Override
    public void setMenuItems() {
        
        inventory.setItem(13, ItemStackBuilder.buildItem(Material.STONE_PICKAXE, plugin.getLangString("GUI.Reset-Percentage-Menu.Items.Current-Percentage.Name").replaceAll("%delay%", String.valueOf(mine.getResetPercentage()))));

        inventory.setItem(6, ItemStackBuilder.buildItem(Material.LEVER, plugin.getLangString("GUI.Reset-Percentage-Menu.Items.Switch.Name"), plugin.getLangStringList("GUI.Reset-Percentage-Menu.Items.Switch.Lore")));


        String increaseBy = plugin.getLangString("GUI.Universal.Increase-By");
        String decreaseBy = plugin.getLangString("GUI.Universal.Decrease-By");

        inventory.setItem(3 * 9 - 8, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "0.01")));
        inventory.setItem(3 * 9 - 7, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "0.1")));
        inventory.setItem(3 * 9 - 6, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "1")));

        inventory.setItem(4 * 9 - 8, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "0.05")));
        inventory.setItem(4 * 9 - 7, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "0.5")));
        inventory.setItem(4 * 9 - 6, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "5")));

        inventory.setItem(5 * 9 - 8, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "0.1")));
        inventory.setItem(5 * 9 - 7, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "1")));
        inventory.setItem(5 * 9 - 6, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "10")));

        inventory.setItem(3 * 9 - 4, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "1")));
        inventory.setItem(3 * 9 - 3, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "0.1")));
        inventory.setItem(3 * 9 - 2, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "0.01")));

        inventory.setItem(4 * 9 - 4, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "5")));
        inventory.setItem(4 * 9 - 3, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "0.5")));
        inventory.setItem(4 * 9 - 2, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "0.05")));

        inventory.setItem(5 * 9 - 4, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "10")));
        inventory.setItem(5 * 9 - 3, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "1")));
        inventory.setItem(5 * 9 - 2, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "0.1")));

        inventory.setItem(45, ItemStackBuilder.buildItem(Material.ARROW, plugin.getLangString("GUI.Universal.Back-To-Mine-Menu.Name"), plugin.getLangStringList("GUI.Universal.Back-To-Mine-Menu.Lore")));
    }
}
