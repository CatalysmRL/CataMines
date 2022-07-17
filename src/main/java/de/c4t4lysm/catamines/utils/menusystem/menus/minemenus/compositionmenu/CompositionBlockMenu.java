package de.c4t4lysm.catamines.utils.menusystem.menus.minemenus.compositionmenu;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.utils.ItemStackBuilder;
import de.c4t4lysm.catamines.utils.menusystem.Menu;
import de.c4t4lysm.catamines.utils.menusystem.PlayerMenuUtility;
import de.c4t4lysm.catamines.utils.menusystem.menus.minemenus.compositionmenu.blockloottable.ChangeBlockLootTableMenu;
import de.c4t4lysm.catamines.utils.menusystem.menus.minemenus.compositionmenu.blockloottable.LootItemListMenu;
import de.c4t4lysm.catamines.utils.mine.components.CataMineBlock;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompositionBlockMenu extends Menu {

    private final CuboidCataMine cuboidCataMine;
    private final CataMineBlock block;
    private final CataMines plugin;

    public CompositionBlockMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        playerMenuUtility.setMenu(this);
        cuboidCataMine = playerMenuUtility.getMine();
        this.block = playerMenuUtility.getBlock();
        this.plugin = CataMines.getInstance();
    }

    @Override
    public String getMenuName() {
        return plugin.getLangString("GUI.Composition-Block-Menu.Title").replaceAll("%blockChance%", String.valueOf(block.getChance())).replaceAll("%compChance%", String.valueOf(cuboidCataMine.getCompositionChance()));
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

        double addPercentage = 0;

        switch (event.getRawSlot()) {
            case 6:
                new ChangeBlockLootTableMenu(playerMenuUtility).open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                return;
            case 7:
                block.setAddLootTable(!block.isAddLootTable());
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                cuboidCataMine.save();
                updateMenus();
                return;
            case 8:
                new LootItemListMenu(playerMenuUtility).open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                return;
            case 45:
                new CompositionMenu(playerMenuUtility).open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                return;
            case 3 * 9 - 8:
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
            case 5 * 9 - 3:
                addPercentage = -1;
                break;
            case 3 * 9 - 3:
            case 5 * 9 - 2:
                addPercentage = -0.1;
                break;
            case 3 * 9 - 2:
                addPercentage = -0.01;
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
            case 5 * 9 - 4:
                addPercentage = -10;
                break;
        }

        if (addPercentage == 0) {
            return;
        }

        try {
            cuboidCataMine.setBlockChance(block, block.getChance() + addPercentage);
        } catch (IllegalArgumentException exception) {
            player.sendMessage(CataMines.PREFIX + exception.getMessage());
        }

        cuboidCataMine.save();
        updateMenus();
    }

    @Override
    public void setMenuItems() {

        inventory.setItem(6, ItemStackBuilder.buildItem(Material.DROPPER, plugin.getLangString("GUI.Composition-Block-Menu.Items.Change-Table.Name")));
        inventory.setItem(7, ItemStackBuilder.buildItem(block.isAddLootTable() ? Material.LIME_DYE : Material.GRAY_DYE,
                block.isAddLootTable() ?
                        plugin.getLangString("GUI.Composition-Block-Menu.Items.Add-Loot-Table.Active.Name") :
                        plugin.getLangString("GUI.Composition-Block-Menu.Items.Add-Loot-Table.Inactive.Name"),
                block.isAddLootTable() ?
                        plugin.getLangStringList("GUI.Composition-Block-Menu.Items.Add-Loot-Table.Active.Lore") :
                        plugin.getLangStringList("GUI.Composition-Block-Menu.Items.Add-Loot-Table.Inactive.Lore")));
        inventory.setItem(8, ItemStackBuilder.buildItem(Material.DISPENSER, plugin.getLangString("GUI.Composition-Block-Menu.Items.Configure-Table.Name")));

        BlockData blockData = block.getBlockData();
        List<String> blockLore = plugin.getLangStringList("GUI.Composition-Block-Menu.Items.Current-Block.Lore");
        blockLore.replaceAll(s -> s.replaceAll("%blockChance%", String.valueOf(block.getChance()))
                .replaceAll("%compChance%", String.valueOf(cuboidCataMine.getCompositionChance()))
                .replaceAll("%blockdata%", blockData.getAsString(true).substring(10 + blockData.getMaterial().name().length())));

        Material material = block.getBlockData().getMaterial();
        if (!material.isItem()) {
            material = Material.WRITTEN_BOOK;
        }

        inventory.setItem(13, ItemStackBuilder.buildItem(material, !material.isSolid() ? ChatColor.WHITE + block.getBlockData().getMaterial().toString() : "",
                blockLore));

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

        inventory.setItem(45, ItemStackBuilder.buildItem(Material.ARROW, plugin.getLangString("GUI.Universal.Back-To-Block-Menu.Name"), plugin.getLangStringList("GUI.Universal.Back-To-Block-Menu.Lore")));
    }
}
