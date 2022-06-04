package de.c4t4lysm.catamines.utils.menusystem.menus;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.utils.ItemStackBuilder;
import de.c4t4lysm.catamines.utils.menusystem.Menu;
import de.c4t4lysm.catamines.utils.menusystem.PlayerMenuUtility;
import de.c4t4lysm.catamines.utils.menusystem.menus.minemenus.*;
import de.c4t4lysm.catamines.utils.menusystem.menus.minemenus.compositionmenu.CompositionMenu;
import de.c4t4lysm.catamines.utils.mine.components.CataMineResetMode;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MineMenu extends Menu {

    private final CuboidCataMine mine;
    private final CataMines plugin = CataMines.getInstance();

    public MineMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        playerMenuUtility.setMenu(this);
        this.mine = playerMenuUtility.getMine();
    }

    public MineMenu(PlayerMenuUtility playerMenuUtility, CuboidCataMine mine) {
        super(playerMenuUtility);
        playerMenuUtility.setMenu(this);
        playerMenuUtility.setMine(mine);
        this.mine = mine;
    }

    @Override
    public String getMenuName() {
        return CataMines.getInstance().getLangString("GUI.Mine-Menu.Title").replaceAll("%mine%", mine.getName());
    }

    @Override
    public int getSlots() {
        return 54;
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
            case 10:
                new CompositionMenu(playerMenuUtility).open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                break;
            case 12:
                if (event.isRightClick()) {
                    if (mine.getResetMode().equals(CataMineResetMode.TIME)) {
                        mine.setResetMode(CataMineResetMode.PERCENTAGE);
                    } else {
                        mine.setResetMode(CataMineResetMode.TIME);
                    }
                    mine.save();
                    updateMenus();
                } else {
                    if (mine.getResetMode().equals(CataMineResetMode.TIME)) {
                        new ResetDelayMenu(playerMenuUtility).open();
                    } else {
                        new ResetPercentageMenu(playerMenuUtility).open();
                    }
                }
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                break;
            case 5:
                mine.setWarn(!mine.isWarn());
                mine.save();
                updateMenus();
                break;
            case 6:
                mine.setWarnGlobal(!mine.isWarnGlobal());
                mine.save();
                updateMenus();
                break;
            case 7:
                mine.setWarnHotbar(!mine.isWarnHotbar());
                mine.save();
                updateMenus();
                break;
            case 14:
                mine.setTeleportPlayers(!mine.isTeleportPlayers());
                mine.save();
                updateMenus();
                break;
            case 15:
                mine.setTeleportPlayersToResetLocation(!mine.isTeleportPlayersToResetLocation());
                mine.save();
                updateMenus();
                break;
            case 16:
                mine.setReplaceMode(!mine.isReplaceMode());
                mine.save();
                updateMenus();
                break;
            case 17:
                new WarnDistanceMenu(playerMenuUtility).open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                break;
            case 8:
                new PickaxeEfficiencyMenu(playerMenuUtility).open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                break;
            case 29:
                player.performCommand("cm reset " + mine.getName());
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                break;
            case 31:
                player.closeInventory();
                if (mine.getTeleportLocation() == null) {
                    player.sendMessage(CataMines.PREFIX + plugin.getLangString("Error-Messages.Mine.Teleport-Error").replaceAll("%mine%", mine.getName()));
                    return;
                }
                player.teleport(mine.getTeleportLocation());
                player.playSound(player.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 0.3F, 1F);
                break;
            case 33:
                new DeleteConfirmMenu(playerMenuUtility).open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                break;
            case 45:
                if (mine.getRegion() == null) {
                    player.sendMessage(CataMines.PREFIX + plugin.getLangString("Error-Messages.Mine.Invalid-Region"));
                    break;
                }
                mine.setStopped(!mine.isStopped());
                mine.save();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                updateMenus();
                break;
            case 49:
                new MineListMenu(playerMenuUtility, playerMenuUtility.getMineListMenuPage()).open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                break;
        }

    }

    @Override
    public void setMenuItems() {

        // ItemStack for configuring the composition
        List<String> compLore = plugin.getLangStringList("GUI.Mine-Menu.Items.Configure-Composition.Lore");
        compLore.replaceAll(s -> s.replaceAll("%chance%", String.valueOf(mine.getCompositionChance())));
        inventory.setItem(10, ItemStackBuilder.buildItem(Material.STONE, plugin.getLangString("GUI.Mine-Menu.Items.Configure-Composition.Name"), compLore));

        if (mine.getResetMode().equals(CataMineResetMode.TIME)) {
            // ItemStack for configuring the reset delay
            List<String> delayLore = plugin.getLangStringList("GUI.Mine-Menu.Items.Configure-Delay.Lore");
            delayLore.replaceAll(s -> s.replaceAll("%delay%", String.valueOf(mine.getResetDelay())).replaceAll("%percentage%", String.valueOf(mine.getResetPercentage())));
            inventory.setItem(12, ItemStackBuilder.buildItem(Material.CLOCK, plugin.getLangString("GUI.Mine-Menu.Items.Configure-Delay.Name"), delayLore));
        } else {
            // ItemStack for configuring the reset percentage
            List<String> percentageLore = plugin.getLangStringList("GUI.Mine-Menu.Items.Configure-Percentage.Lore");
            percentageLore.replaceAll(s -> s.replaceAll("%percentage%", String.valueOf(mine.getResetPercentage())).replaceAll("%delay%", String.valueOf(mine.getResetDelay())));
            inventory.setItem(12, ItemStackBuilder.buildItem(Material.STONE_PICKAXE, plugin.getLangString("GUI.Mine-Menu.Items.Configure-Percentage.Name"), percentageLore));
        }

        // Configure Attributes
        // Select warn
        inventory.setItem(5, ItemStackBuilder.buildItem(mine.isWarn() ? Material.LIME_DYE : Material.GRAY_DYE, mine.isWarn() ? plugin.getLangString("GUI.Mine-Menu.Items.Warn.Active.Name") : plugin.getLangString("GUI.Mine-Menu.Items.Warn.Inactive.Name"), plugin.getLangStringList("GUI.Mine-Menu.Items.Warn.Lore")));
        // Select global warn
        inventory.setItem(6, ItemStackBuilder.buildItem(mine.isWarnGlobal() ? Material.LIME_DYE : Material.GRAY_DYE, mine.isWarnGlobal() ? plugin.getLangString("GUI.Mine-Menu.Items.Warn-Global.Active.Name") : plugin.getLangString("GUI.Mine-Menu.Items.Warn-Global.Inactive.Name"), plugin.getLangStringList("GUI.Mine-Menu.Items.Warn-Global.Lore")));
        // Select warn HotBar
        inventory.setItem(7, ItemStackBuilder.buildItem(mine.isWarnHotbar() ? Material.LIME_DYE : Material.GRAY_DYE, mine.isWarnHotbar() ? plugin.getLangString("GUI.Mine-Menu.Items.Warn-Hotbar.Active.Name") : plugin.getLangString("GUI.Mine-Menu.Items.Warn-Hotbar.Inactive.Name"), plugin.getLangStringList("GUI.Mine-Menu.Items.Warn-Hotbar.Lore")));
        // Select teleport players
        inventory.setItem(14, ItemStackBuilder.buildItem(mine.isTeleportPlayers() ? Material.LIME_DYE : Material.GRAY_DYE, mine.isTeleportPlayers() ? plugin.getLangString("GUI.Mine-Menu.Items.Teleport-Players.Active.Name") : plugin.getLangString("GUI.Mine-Menu.Items.Teleport-Players.Inactive.Name"), plugin.getLangStringList("GUI.Mine-Menu.Items.Teleport-Players.Lore")));
        // Select teleport players to reset location
        inventory.setItem(15, ItemStackBuilder.buildItem(mine.isTeleportPlayersToResetLocation() ? Material.LIME_DYE : Material.GRAY_DYE, mine.isTeleportPlayersToResetLocation() ? plugin.getLangString("GUI.Mine-Menu.Items.Teleport-Players-To-Reset-Location.Active.Name") : plugin.getLangString("GUI.Mine-Menu.Items.Teleport-Players-To-Reset-Location.Inactive.Name"), plugin.getLangStringList("GUI.Mine-Menu.Items.Teleport-Players-To-Reset-Location.Lore")));
        // Select replace mode
        inventory.setItem(16, ItemStackBuilder.buildItem(mine.isReplaceMode() ? Material.LIME_DYE : Material.GRAY_DYE, mine.isReplaceMode() ? plugin.getLangString("GUI.Mine-Menu.Items.Replace-Mode.Active.Name") : plugin.getLangString("GUI.Mine-Menu.Items.Replace-Mode.Inactive.Name"), plugin.getLangStringList("GUI.Mine-Menu.Items.Replace-Mode.Lore")));

        // ItemStack for configuring warn distance
        List<String> warnDistanceLore = plugin.getLangStringList("GUI.Mine-Menu.Items.Warn-Distance.Lore");
        warnDistanceLore.replaceAll(s -> s.replaceAll("%distance%", String.valueOf(mine.getWarnDistance())));
        inventory.setItem(17, ItemStackBuilder.buildItem(Material.STICK, plugin.getLangString("GUI.Mine-Menu.Items.Warn-Distance.Name"), warnDistanceLore));

        // ItemStack for configuring min. efficiency level
        List<String> efficiencyLore = plugin.getLangStringList("GUI.Mine-Menu.Items.Efficiency-Lvl.Lore");
        efficiencyLore.replaceAll(s -> s.replaceAll("%level%", String.valueOf(mine.getMinEfficiencyLvl())));
        inventory.setItem(8, ItemStackBuilder.buildItem(Material.DIAMOND_PICKAXE, plugin.getLangString("GUI.Mine-Menu.Items.Efficiency-Lvl.Name"), efficiencyLore));

        // Reset mine
        inventory.setItem(29, ItemStackBuilder.buildItem(Material.REDSTONE, plugin.getLangString("GUI.Mine-Menu.Items.Reset-Mine.Name")));
        // Teleport to mine
        inventory.setItem(31, ItemStackBuilder.buildItem(Material.ENDER_PEARL, plugin.getLangString("GUI.Mine-Menu.Items.Teleport.Name"), plugin.getLangStringList("GUI.Mine-Menu.Items.Teleport.Lore")));
        // Delete mine
        inventory.setItem(33, ItemStackBuilder.buildItem(Material.REDSTONE_BLOCK, plugin.getLangString("GUI.Mine-Menu.Items.Delete.Name")));

        // De-/Activate
        inventory.setItem(45, ItemStackBuilder.buildItem(!mine.isStopped() ? Material.REDSTONE_TORCH : Material.LEVER, !mine.isStopped() ? plugin.getLangString("GUI.Mine-Menu.Items.Start-Stop.Active.Name") : ChatColor.GRAY + plugin.getLangString("GUI.Mine-Menu.Items.Start-Stop.Inactive.Name"), plugin.getLangStringList("GUI.Mine-Menu.Items.Start-Stop.Lore")));

        // Go back to mine list
        inventory.setItem(49, ItemStackBuilder.buildItem(Material.BARRIER, plugin.getLangString("GUI.Mine-Menu.Items.Back.Name"), plugin.getLangStringList("GUI.Mine-Menu.Items.Back.Lore")));

        // Displays if the mine could run
        if (mine.checkRunnable() && !mine.isStopped()) {
            inventory.setItem(53, ItemStackBuilder.buildItem(Material.LIME_DYE, plugin.getLangString("GUI.Mine-Menu.Items.Running.Active.Name"), plugin.getLangStringList("GUI.Mine-Menu.Items.Running.Active.Lore")));
        } else {

            ArrayList<String> reasons = new ArrayList<>();

            if (mine.getRegion() == null) {
                reasons.add(plugin.getLangString("GUI.Mine-Menu.Items.Running.Inactive.Reasons.Invalid-Region"));
            }
            if (mine.isStopped()) {
                reasons.add(plugin.getLangString("GUI.Mine-Menu.Items.Running.Inactive.Reasons.Stopped"));
            }
            if (mine.getRandomPattern() == null) {
                reasons.add(plugin.getLangString("GUI.Mine-Menu.Items.Running.Inactive.Reasons.No-Composition"));
            }
            if (!(mine.getResetDelay() > 0 && mine.getResetDelay() <= 100000)) {
                reasons.add(plugin.getLangString("GUI.Mine-Menu.Items.Running.Inactive.Reasons.Invalid-Delay").replaceAll("%delay%", String.valueOf(mine.getResetDelay())));
            }

            inventory.setItem(53, ItemStackBuilder.buildItem(Material.GRAY_DYE, plugin.getLangString("GUI.Mine-Menu.Items.Running.Inactive.Name"), reasons));
        }

    }
}
