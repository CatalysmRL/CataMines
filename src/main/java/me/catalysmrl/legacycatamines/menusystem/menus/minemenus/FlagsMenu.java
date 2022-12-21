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

import java.util.List;

public class FlagsMenu extends Menu {

    private final CuboidCataMine mine;
    private final CataMines plugin;

    public FlagsMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        playerMenuUtility.setMenu(this);
        mine = playerMenuUtility.getMine();
        plugin = CataMines.getInstance();
    }

    @Override
    public String getMenuName() {
        return plugin.getLangString("GUI.Flags-Menu.Title");
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        switch (event.getRawSlot()) {
            case 19:
                mine.setWarn(!mine.isWarn());
                break;
            case 10:
                mine.setWarnGlobal(!mine.isWarnGlobal());
                break;
            case 28:
                mine.setWarnHotbar(!mine.isWarnHotbar());
                break;
            case 21:
                mine.setTeleportPlayers(!mine.isTeleportPlayers());
                break;
            case 30:
                mine.setTeleportPlayersToResetLocation(!mine.isTeleportPlayersToResetLocation());
                break;
            case 23:
                mine.setReplaceMode(!mine.isReplaceMode());
                break;
            case 25:
                new WarnDistanceMenu(playerMenuUtility).open();
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                return;
            case 36:
                new MineMenu(playerMenuUtility).open();
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                return;
        }

        mine.save();
        updateMenus();
    }

    @Override
    public void setMenuItems() {

        // Enable warn
        if (mine.isWarn()) {
            inventory.setItem(19, ItemStackBuilder.buildItem(
                    Material.LIME_DYE,
                    plugin.getLangString("GUI.Flags-Menu.Items.Warn.Active.Name"),
                    plugin.getLangStringList("GUI.Flags-Menu.Items.Warn.Active.Lore")));
        } else {
            inventory.setItem(19, ItemStackBuilder.buildItem(
                    Material.GRAY_DYE,
                    plugin.getLangString("GUI.Flags-Menu.Items.Warn.Inactive.Name"),
                    plugin.getLangStringList("GUI.Flags-Menu.Items.Warn.Inactive.Lore")));
        }

        // Warn globally
        if (mine.isWarnGlobal()) {
            inventory.setItem(10, ItemStackBuilder.buildItem(
                    Material.LIME_DYE,
                    plugin.getLangString("GUI.Flags-Menu.Items.Warn-Global.Active.Name"),
                    plugin.getLangStringList("GUI.Flags-Menu.Items.Warn-Global.Active.Lore")));
        } else {
            inventory.setItem(10, ItemStackBuilder.buildItem(
                    Material.GRAY_DYE,
                    plugin.getLangString("GUI.Flags-Menu.Items.Warn-Global.Inactive.Name"),
                    plugin.getLangStringList("GUI.Flags-Menu.Items.Warn-Global.Inactive.Lore")));
        }

        // Action bar
        if (mine.isWarnHotbar()) {
            inventory.setItem(28, ItemStackBuilder.buildItem(
                    Material.LIME_DYE,
                    plugin.getLangString("GUI.Flags-Menu.Items.Warn-Hotbar.Active.Name"),
                    plugin.getLangStringList("GUI.Flags-Menu.Items.Warn-Hotbar.Active.Lore")));
        } else {
            inventory.setItem(28, ItemStackBuilder.buildItem(
                    Material.GRAY_DYE,
                    plugin.getLangString("GUI.Flags-Menu.Items.Warn-Hotbar.Inactive.Name"),
                    plugin.getLangStringList("GUI.Flags-Menu.Items.Warn-Hotbar.Inactive.Lore")));
        }

        // Teleport players
        if (mine.isTeleportPlayers()) {
            inventory.setItem(21, ItemStackBuilder.buildItem(
                    Material.LIME_DYE,
                    plugin.getLangString("GUI.Flags-Menu.Items.Teleport-Players.Active.Name"),
                    plugin.getLangStringList("GUI.Flags-Menu.Items.Teleport-Players.Active.Lore")));
        } else {
            inventory.setItem(21, ItemStackBuilder.buildItem(
                    Material.GRAY_DYE,
                    plugin.getLangString("GUI.Flags-Menu.Items.Teleport-Players.Inactive.Name"),
                    plugin.getLangStringList("GUI.Flags-Menu.Items.Teleport-Players.Inactive.Lore")));
        }

        // Teleport players to reset location
        if (mine.isTeleportPlayersToResetLocation()) {
            inventory.setItem(30, ItemStackBuilder.buildItem(
                    Material.LIME_DYE,
                    plugin.getLangString("GUI.Flags-Menu.Items.Teleport-Players-To-Reset-Location.Active.Name"),
                    plugin.getLangStringList("GUI.Flags-Menu.Items.Teleport-Players-To-Reset-Location.Active.Lore")));
        } else {
            inventory.setItem(30, ItemStackBuilder.buildItem(
                    Material.GRAY_DYE,
                    plugin.getLangString("GUI.Flags-Menu.Items.Teleport-Players-To-Reset-Location.Inactive.Name"),
                    plugin.getLangStringList("GUI.Flags-Menu.Items.Teleport-Players-To-Reset-Location.Inactive.Lore")));
        }

        // Replace mode
        if (mine.isReplaceMode()) {
            inventory.setItem(23, ItemStackBuilder.buildItem(
                    Material.LIME_DYE,
                    plugin.getLangString("GUI.Flags-Menu.Items.Replace-Mode.Active.Name"),
                    plugin.getLangStringList("GUI.Flags-Menu.Items.Replace-Mode.Active.Lore")));
        } else {
            inventory.setItem(23, ItemStackBuilder.buildItem(
                    Material.GRAY_DYE,
                    plugin.getLangString("GUI.Flags-Menu.Items.Replace-Mode.Inactive.Name"),
                    plugin.getLangStringList("GUI.Flags-Menu.Items.Replace-Mode.Inactive.Lore")));
        }

        // Warn distance
        List<String> warnDistanceLore = plugin.getLangStringList("GUI.Flags-Menu.Items.Warn-Distance.Lore");
        warnDistanceLore.replaceAll(s -> s.replaceAll("%distance%", String.valueOf(mine.getWarnDistance())));
        inventory.setItem(25, ItemStackBuilder.buildItem(Material.STICK, plugin.getLangString("GUI.Flags-Menu.Items.Warn-Distance.Name"), warnDistanceLore));

        // Back arrow
        inventory.setItem(36, ItemStackBuilder.buildItem(
                Material.ARROW,
                plugin.getLangString("GUI.Universal.Back-To-Mine-Menu.Name"),
                plugin.getLangStringList("GUI.Universal.Back-To-Mine-Menu.Lore")));

    }
}
