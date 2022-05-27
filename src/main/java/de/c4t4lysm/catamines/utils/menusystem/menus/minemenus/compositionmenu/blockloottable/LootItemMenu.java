package de.c4t4lysm.catamines.utils.menusystem.menus.minemenus.compositionmenu.blockloottable;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.utils.ItemStackBuilder;
import de.c4t4lysm.catamines.utils.menusystem.Menu;
import de.c4t4lysm.catamines.utils.menusystem.PlayerMenuUtility;
import de.c4t4lysm.catamines.utils.mine.components.CataMineLootItem;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class LootItemMenu extends Menu {

    private final CuboidCataMine mine;
    private final CataMineLootItem item;

    public LootItemMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        playerMenuUtility.setMenu(this);
        this.mine = playerMenuUtility.getMine();
        this.item = playerMenuUtility.getItem();
    }

    @Override
    public String getMenuName() {
        return CataMines.getInstance().getLangString("GUI.Configure-Item-Drop-Chances-Menu.Title").replaceAll("%itemChance%", String.valueOf(item.getChance()));
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);
        double addPercentage = 0;
        switch (event.getRawSlot()) {
            case 45:
                new LootItemListMenu(playerMenuUtility).open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                return;
            case 3 * 9 - 9:
                addPercentage = 0.001;
                break;
            case 3 * 9 - 8:
            case 5 * 9 - 9:
                addPercentage = 0.01;
                break;
            case 3 * 9 - 7:
            case 5 * 9 - 8:
                addPercentage = 0.1;
                break;
            case 3 * 9 - 6:
            case 5 * 9 - 7:
                addPercentage = 1;
                break;
            case 4 * 9 - 9:
                addPercentage = 0.005;
                break;
            case 4 * 9 - 8:
                addPercentage = 0.05;
                break;
            case 4 * 9 - 7:
                addPercentage = 0.5;
                break;
            case 4 * 9 - 6:
                addPercentage = 5;
                break;
            case 5 * 9 - 6:
                addPercentage = 10;
                break;
            case 3 * 9 - 4:
                addPercentage = -1;
                break;
            case 3 * 9 - 3:
            case 5 * 9 - 2:
                addPercentage = -0.1;
                break;
            case 3 * 9 - 2:
            case 5 * 9 - 1:
                addPercentage = -0.01;
                break;
            case 3 * 9 - 1:
                addPercentage = -0.001;
                break;
            case 4 * 9 - 4:
                addPercentage = -5;
                break;
            case 4 * 9 - 3:
                addPercentage = -0.5;
                break;
            case 4 * 9 - 2:
                addPercentage = -0.05;
                break;
            case 4 * 9 - 1:
                addPercentage = -0.005;
                break;
            case 5 * 9 - 4:
                addPercentage = -10;
                break;
        }

        if (addPercentage == 0) {
            return;
        }
        try {
            item.setChance(item.getChance() + addPercentage);
        } catch (IllegalArgumentException exception) {
            event.getWhoClicked().sendMessage(CataMines.PREFIX + exception.getMessage());
        }
        mine.save();
        updateMenus();
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(13, item.getItem());

        String increaseBy = CataMines.getInstance().getLangString("GUI.Universal.Increase-By");
        String decreaseBy = CataMines.getInstance().getLangString("GUI.Universal.Decrease-By");

        inventory.setItem(3 * 9 - 9, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "0.001")));
        inventory.setItem(3 * 9 - 8, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "0.01")));
        inventory.setItem(3 * 9 - 7, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "0.1")));
        inventory.setItem(3 * 9 - 6, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "1")));

        inventory.setItem(4 * 9 - 9, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "0.005")));
        inventory.setItem(4 * 9 - 8, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "0.05")));
        inventory.setItem(4 * 9 - 7, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "0.5")));
        inventory.setItem(4 * 9 - 6, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "5")));

        inventory.setItem(5 * 9 - 9, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "0.01")));
        inventory.setItem(5 * 9 - 8, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "0.1")));
        inventory.setItem(5 * 9 - 7, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "1")));
        inventory.setItem(5 * 9 - 6, ItemStackBuilder.buildItem(Material.LIME_WOOL, increaseBy.replaceAll("%number%", "10")));

        inventory.setItem(3 * 9 - 4, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "1")));
        inventory.setItem(3 * 9 - 3, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "0.1")));
        inventory.setItem(3 * 9 - 2, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "0.01")));
        inventory.setItem(3 * 9 - 1, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "0.001")));

        inventory.setItem(4 * 9 - 4, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "5")));
        inventory.setItem(4 * 9 - 3, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "0.5")));
        inventory.setItem(4 * 9 - 2, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "0.05")));
        inventory.setItem(4 * 9 - 1, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "0.005")));

        inventory.setItem(5 * 9 - 4, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "10")));
        inventory.setItem(5 * 9 - 3, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "1")));
        inventory.setItem(5 * 9 - 2, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "0.1")));
        inventory.setItem(5 * 9 - 1, ItemStackBuilder.buildItem(Material.RED_WOOL, decreaseBy.replaceAll("%number%", "0.01")));

        inventory.setItem(45, ItemStackBuilder.buildItem(Material.ARROW, CataMines.getInstance().getLangString("GUI.Universal.Back-To-Block-Menu.Name"), CataMines.getInstance().getLangStringList("GUI.Universal.Back-To-Block-Menu.Lore")));
    }
}
