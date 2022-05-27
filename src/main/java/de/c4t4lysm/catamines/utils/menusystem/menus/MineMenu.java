package de.c4t4lysm.catamines.utils.menusystem.menus;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.utils.ItemStackBuilder;
import de.c4t4lysm.catamines.utils.menusystem.Menu;
import de.c4t4lysm.catamines.utils.menusystem.PlayerMenuUtility;
import de.c4t4lysm.catamines.utils.menusystem.menus.minemenus.DeleteConfirmMenu;
import de.c4t4lysm.catamines.utils.menusystem.menus.minemenus.PickaxeEfficiencyMenu;
import de.c4t4lysm.catamines.utils.menusystem.menus.minemenus.ResetDelayMenu;
import de.c4t4lysm.catamines.utils.menusystem.menus.minemenus.WarnDistanceMenu;
import de.c4t4lysm.catamines.utils.menusystem.menus.minemenus.compositionmenu.CompositionMenu;
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

    private final CuboidCataMine cuboidCataMine;
    private final CataMines plugin = CataMines.getInstance();

    public MineMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        playerMenuUtility.setMenu(this);
        this.cuboidCataMine = playerMenuUtility.getMine();
    }

    public MineMenu(PlayerMenuUtility playerMenuUtility, CuboidCataMine cuboidCataMine) {
        super(playerMenuUtility);
        playerMenuUtility.setMenu(this);
        playerMenuUtility.setMine(cuboidCataMine);
        this.cuboidCataMine = cuboidCataMine;
    }

    @Override
    public String getMenuName() {
        return CataMines.getInstance().getLangString("GUI.Mine-Menu.Title").replaceAll("%mine%", cuboidCataMine.getName());
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
                new ResetDelayMenu(playerMenuUtility).open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                break;
            case 5:
                cuboidCataMine.setWarn(!cuboidCataMine.isWarn());
                cuboidCataMine.save();
                updateMenus();
                break;
            case 6:
                cuboidCataMine.setWarnGlobal(!cuboidCataMine.isWarnGlobal());
                cuboidCataMine.save();
                updateMenus();
                break;
            case 7:
                cuboidCataMine.setWarnHotbar(!cuboidCataMine.isWarnHotbar());
                cuboidCataMine.save();
                updateMenus();
                break;
            case 14:
                cuboidCataMine.setTeleportPlayers(!cuboidCataMine.isTeleportPlayers());
                cuboidCataMine.save();
                updateMenus();
                break;
            case 15:
                cuboidCataMine.setTeleportPlayersToResetLocation(!cuboidCataMine.isTeleportPlayersToResetLocation());
                cuboidCataMine.save();
                updateMenus();
                break;
            case 16:
                cuboidCataMine.setReplaceMode(!cuboidCataMine.isReplaceMode());
                cuboidCataMine.save();
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
                player.performCommand("cm reset " + cuboidCataMine.getName());
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                break;
            case 31:
                player.closeInventory();
                if (cuboidCataMine.getTeleportLocation() == null) {
                    player.sendMessage(CataMines.PREFIX + plugin.getLangString("Error-Messages.Mine.Teleport-Error").replaceAll("%mine%", cuboidCataMine.getName()));
                    return;
                }
                player.teleport(cuboidCataMine.getTeleportLocation());
                player.playSound(player.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 0.3F, 1F);
                break;
            case 33:
                new DeleteConfirmMenu(playerMenuUtility).open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                break;
            case 45:
                if (cuboidCataMine.getRegion() == null) {
                    player.sendMessage(CataMines.PREFIX + plugin.getLangString("Error-Messages.Mine.Invalid-Region"));
                    break;
                }
                cuboidCataMine.setStopped(!cuboidCataMine.isStopped());
                cuboidCataMine.save();
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
        compLore.replaceAll(s -> s.replaceAll("%chance%", String.valueOf(cuboidCataMine.getCompositionChance())));
        inventory.setItem(10, ItemStackBuilder.buildItem(Material.STONE, plugin.getLangString("GUI.Mine-Menu.Items.Configure-Composition.Name"), compLore));

        // ItemStack for configuring the reset delay
        List<String> delayLore = plugin.getLangStringList("GUI.Mine-Menu.Items.Configure-Delay.Lore");
        delayLore.replaceAll(s -> s.replaceAll("%delay%", String.valueOf(cuboidCataMine.getResetDelay())));
        inventory.setItem(12, ItemStackBuilder.buildItem(Material.CLOCK, plugin.getLangString("GUI.Mine-Menu.Items.Configure-Delay.Name"), delayLore));

        // Configure Attributes
        // Select warn
        inventory.setItem(5, ItemStackBuilder.buildItem(cuboidCataMine.isWarn() ? Material.LIME_DYE : Material.GRAY_DYE, cuboidCataMine.isWarn() ? plugin.getLangString("GUI.Mine-Menu.Items.Warn.Active.Name") : plugin.getLangString("GUI.Mine-Menu.Items.Warn.Inactive.Name"), plugin.getLangStringList("GUI.Mine-Menu.Items.Warn.Lore")));
        // Select global warn
        inventory.setItem(6, ItemStackBuilder.buildItem(cuboidCataMine.isWarnGlobal() ? Material.LIME_DYE : Material.GRAY_DYE, cuboidCataMine.isWarnGlobal() ? plugin.getLangString("GUI.Mine-Menu.Items.Warn-Global.Active.Name") : plugin.getLangString("GUI.Mine-Menu.Items.Warn-Global.Inactive.Name"), plugin.getLangStringList("GUI.Mine-Menu.Items.Warn-Global.Lore")));
        // Select warn HotBar
        inventory.setItem(7, ItemStackBuilder.buildItem(cuboidCataMine.isWarnHotbar() ? Material.LIME_DYE : Material.GRAY_DYE, cuboidCataMine.isWarnHotbar() ? plugin.getLangString("GUI.Mine-Menu.Items.Warn-Hotbar.Active.Name") : plugin.getLangString("GUI.Mine-Menu.Items.Warn-Hotbar.Inactive.Name"), plugin.getLangStringList("GUI.Mine-Menu.Items.Warn-Hotbar.Lore")));
        // Select teleport players
        inventory.setItem(14, ItemStackBuilder.buildItem(cuboidCataMine.isTeleportPlayers() ? Material.LIME_DYE : Material.GRAY_DYE, cuboidCataMine.isTeleportPlayers() ? plugin.getLangString("GUI.Mine-Menu.Items.Teleport-Players.Active.Name") : plugin.getLangString("GUI.Mine-Menu.Items.Teleport-Players.Inactive.Name"), plugin.getLangStringList("GUI.Mine-Menu.Items.Teleport-Players.Lore")));
        // Select teleport players to reset location
        inventory.setItem(15, ItemStackBuilder.buildItem(cuboidCataMine.isTeleportPlayersToResetLocation() ? Material.LIME_DYE : Material.GRAY_DYE, cuboidCataMine.isTeleportPlayersToResetLocation() ? plugin.getLangString("GUI.Mine-Menu.Items.Teleport-Players-To-Reset-Location.Active.Name") : plugin.getLangString("GUI.Mine-Menu.Items.Teleport-Players-To-Reset-Location.Inactive.Name"), plugin.getLangStringList("GUI.Mine-Menu.Items.Teleport-Players-To-Reset-Location.Lore")));
        // Select replace mode
        inventory.setItem(16, ItemStackBuilder.buildItem(cuboidCataMine.isReplaceMode() ? Material.LIME_DYE : Material.GRAY_DYE, cuboidCataMine.isReplaceMode() ? plugin.getLangString("GUI.Mine-Menu.Items.Replace-Mode.Active.Name") : plugin.getLangString("GUI.Mine-Menu.Items.Replace-Mode.Inactive.Name"), plugin.getLangStringList("GUI.Mine-Menu.Items.Replace-Mode.Lore")));

        // ItemStack for configuring warn distance
        List<String> warnDistanceLore = plugin.getLangStringList("GUI.Mine-Menu.Items.Warn-Distance.Lore");
        warnDistanceLore.replaceAll(s -> s.replaceAll("%distance%", String.valueOf(cuboidCataMine.getWarnDistance())));
        inventory.setItem(17, ItemStackBuilder.buildItem(Material.STICK, plugin.getLangString("GUI.Mine-Menu.Items.Warn-Distance.Name"), warnDistanceLore));

        // ItemStack for configuring min. efficiency level
        List<String> efficiencyLore = plugin.getLangStringList("GUI.Mine-Menu.Items.Efficiency-Lvl.Lore");
        efficiencyLore.replaceAll(s -> s.replaceAll("%level%", String.valueOf(cuboidCataMine.getMinEfficiencyLvl())));
        inventory.setItem(8, ItemStackBuilder.buildItem(Material.DIAMOND_PICKAXE, plugin.getLangString("GUI.Mine-Menu.Items.Efficiency-Lvl.Name"), efficiencyLore));

        // Reset mine
        inventory.setItem(29, ItemStackBuilder.buildItem(Material.REDSTONE, plugin.getLangString("GUI.Mine-Menu.Items.Reset-Mine.Name")));
        // Teleport to mine
        inventory.setItem(31, ItemStackBuilder.buildItem(Material.ENDER_PEARL, plugin.getLangString("GUI.Mine-Menu.Items.Teleport.Name"), plugin.getLangStringList("GUI.Mine-Menu.Items.Teleport.Lore")));
        // Delete mine
        inventory.setItem(33, ItemStackBuilder.buildItem(Material.REDSTONE_BLOCK, plugin.getLangString("GUI.Mine-Menu.Items.Delete.Name")));

        // De-/Activate
        inventory.setItem(45, ItemStackBuilder.buildItem(!cuboidCataMine.isStopped() ? Material.REDSTONE_TORCH : Material.LEVER, !cuboidCataMine.isStopped() ? plugin.getLangString("GUI.Mine-Menu.Items.Start-Stop.Active.Name") : ChatColor.GRAY + plugin.getLangString("GUI.Mine-Menu.Items.Start-Stop.Inactive.Name"), plugin.getLangStringList("GUI.Mine-Menu.Items.Start-Stop.Lore")));

        // Go back to mine list
        inventory.setItem(49, ItemStackBuilder.buildItem(Material.BARRIER, plugin.getLangString("GUI.Mine-Menu.Items.Back.Name"), plugin.getLangStringList("GUI.Mine-Menu.Items.Back.Lore")));

        // Displays if the mine could run
        if (cuboidCataMine.checkRunnable() && !cuboidCataMine.isStopped()) {
            inventory.setItem(53, ItemStackBuilder.buildItem(Material.LIME_DYE, plugin.getLangString("GUI.Mine-Menu.Items.Running.Active.Name"), plugin.getLangStringList("GUI.Mine-Menu.Items.Running.Active.Lore")));
        } else {

            ArrayList<String> reasons = new ArrayList<>();

            if (cuboidCataMine.getRegion() == null) {
                reasons.add(plugin.getLangString("GUI.Mine-Menu.Items.Running.Inactive.Reasons.Invalid-Region"));
            }
            if (cuboidCataMine.isStopped()) {
                reasons.add(plugin.getLangString("GUI.Mine-Menu.Items.Running.Inactive.Reasons.Stopped"));
            }
            if (cuboidCataMine.getRandomPattern() == null) {
                reasons.add(plugin.getLangString("GUI.Mine-Menu.Items.Running.Inactive.Reasons.No-Composition"));
            }
            if (!(cuboidCataMine.getResetDelay() > 0 && cuboidCataMine.getResetDelay() <= 100000)) {
                reasons.add(plugin.getLangString("GUI.Mine-Menu.Items.Running.Inactive.Reasons.Invalid-Delay").replaceAll("%delay%", String.valueOf(cuboidCataMine.getResetDelay())));
            }

            inventory.setItem(53, ItemStackBuilder.buildItem(Material.GRAY_DYE, plugin.getLangString("GUI.Mine-Menu.Items.Running.Inactive.Name"), reasons));
        }

    }
}
