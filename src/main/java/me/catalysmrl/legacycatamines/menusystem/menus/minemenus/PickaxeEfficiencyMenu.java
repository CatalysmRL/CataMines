package me.catalysmrl.legacycatamines.menusystem.menus.minemenus;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.utils.helper.ItemStackBuilder;
import me.catalysmrl.legacycatamines.menusystem.Menu;
import me.catalysmrl.legacycatamines.menusystem.PlayerMenuUtility;
import me.catalysmrl.legacycatamines.menusystem.menus.MineMenu;
import me.catalysmrl.legacycatamines.mine.mines.CuboidCataMine;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class PickaxeEfficiencyMenu extends Menu {

    private final CuboidCataMine mine;
    private final CataMines plugin;

    public PickaxeEfficiencyMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        playerMenuUtility.setMenu(this);
        mine = playerMenuUtility.getMine();
        this.plugin = CataMines.getInstance();
    }

    @Override
    public String getMenuName() {
        return plugin.getLangString("GUI.Efficiency-Menu.Title").replaceAll("%level%", String.valueOf(mine.getMinEfficiencyLvl()));
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

        int addLevel = 0;

        switch (event.getRawSlot()) {
            case 45:
                new MineMenu(playerMenuUtility).open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                return;
            case 3 * 9 - 6:
                addLevel = 1;
                break;
            case 4 * 9 - 6:
                addLevel = 5;
                break;
            case 5 * 9 - 6:
                addLevel = 10;
                break;
            case 3 * 9 - 4:
                addLevel = -1;
                break;
            case 4 * 9 - 4:
                addLevel = -5;
                break;
            case 5 * 9 - 4:
                addLevel = -10;
                break;
        }

        if (addLevel == 0) {
            return;
        }

        mine.setMinEfficiencyLvl(mine.getMinEfficiencyLvl() + addLevel);
        mine.save();
        updateMenus();
    }

    @Override
    public void setMenuItems() {
        List<String> efficiencyLore = plugin.getLangStringList("GUI.Efficiency-Menu.Items.Current-Level.Lore");
        efficiencyLore.replaceAll(s -> s.replaceAll("%level%", String.valueOf(mine.getMinEfficiencyLvl())));
        inventory.setItem(13, ItemStackBuilder.buildItem(Material.DIAMOND_PICKAXE, plugin.getLangString("GUI.Efficiency-Menu.Items.Current-Level.Name").replaceAll("%level%", String.valueOf(mine.getMinEfficiencyLvl())),
                efficiencyLore));

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
