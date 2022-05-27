package de.c4t4lysm.catamines.utils.menusystem.menus.minemenus;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.utils.ItemStackBuilder;
import de.c4t4lysm.catamines.utils.menusystem.Menu;
import de.c4t4lysm.catamines.utils.menusystem.PlayerMenuUtility;
import de.c4t4lysm.catamines.utils.menusystem.menus.MineMenu;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class WarnDistanceMenu extends Menu {

    private final CuboidCataMine mine;
    private final CataMines plugin;

    public WarnDistanceMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        playerMenuUtility.setMenu(this);
        this.mine = playerMenuUtility.getMine();
        this.plugin = CataMines.getInstance();
    }

    @Override
    public String getMenuName() {
        return plugin.getLangString("GUI.Warn-Distance-Menu.Title").replaceAll("%distance%", String.valueOf(mine.getWarnDistance()));
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || Objects.equals(event.getClickedInventory(), event.getWhoClicked().getInventory())) {
            return;
        }

        int addWarnDistance = 0;

        switch (event.getRawSlot()) {
            case 45:
                new MineMenu(playerMenuUtility).open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                return;
            case 3 * 9 - 6:
                addWarnDistance = 1;
                break;
            case 4 * 9 - 6:
                addWarnDistance = 5;
                break;
            case 5 * 9 - 6:
                addWarnDistance = 10;
                break;
            case 3 * 9 - 4:
                addWarnDistance = -1;
                break;
            case 4 * 9 - 4:
                addWarnDistance = -5;
                break;
            case 5 * 9 - 4:
                addWarnDistance = -10;
                break;
        }

        if (addWarnDistance == 0) {
            return;
        }

        mine.setWarnDistance(mine.getWarnDistance() + addWarnDistance);
        mine.save();
        updateMenus();
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(13, ItemStackBuilder.buildItem(Material.SIGN, plugin.getLangString("GUI.Warn-Distance-Menu.Items.Current-Distance.Name").replaceAll("%distance%", String.valueOf(mine.getWarnDistance()))));

        String increaseBy = plugin.getLangString("GUI.Universal.Increase-By");
        String decreaseBy = plugin.getLangString("GUI.Universal.Decrease-By");

        inventory.setItem(3 * 9 - 6, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "1")));
        inventory.setItem(4 * 9 - 6, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "5")));
        inventory.setItem(5 * 9 - 6, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "10")));

        inventory.setItem(3 * 9 - 4, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "1")));
        inventory.setItem(4 * 9 - 4, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "5")));
        inventory.setItem(5 * 9 - 4, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "10")));

        inventory.setItem(45, ItemStackBuilder.buildItem(Material.ARROW, plugin.getLangString("GUI.Universal.Back-To-Mine-Menu.Name"), plugin.getLangStringList("GUI.Universal.Back-To-Mine-Menu.Lore")));
    }
}
