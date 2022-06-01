package de.c4t4lysm.catamines.utils.menusystem.menus.minemenus.compositionmenu;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.utils.ItemStackBuilder;
import de.c4t4lysm.catamines.utils.menusystem.PaginatedMenu;
import de.c4t4lysm.catamines.utils.menusystem.PlayerMenuUtility;
import de.c4t4lysm.catamines.utils.menusystem.menus.MineMenu;
import de.c4t4lysm.catamines.utils.mine.components.CataMineBlock;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.Bukkit;
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

public class CompositionMenu extends PaginatedMenu {

    private final CuboidCataMine mine;
    private final CataMines plugin;

    public CompositionMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        playerMenuUtility.setMenu(this);
        mine = playerMenuUtility.getMine();
        plugin = CataMines.getInstance();
    }

    @Override
    public String getMenuName() {
        return plugin.getLangString("GUI.Composition-Menu.Title").replaceAll("%chance%", String.valueOf(mine.getCompositionChance()));
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) {
            return;
        }

        ArrayList<Material> materials = new ArrayList<>();
        mine.getBlocks().forEach(block -> materials.add(block.getBlockData().getMaterial()));

        Player player = (Player) event.getWhoClicked();
        if (Objects.equals(event.getClickedInventory(), player.getInventory())) {

            BlockData blockData = event.getCurrentItem().getType().createBlockData();

            if (blockData.getMaterial().equals(Material.BONE_MEAL)) {
                blockData = Material.AIR.createBlockData();
            }

            if (mine.containsBlockData(blockData)) {
                player.sendMessage(CataMines.PREFIX + plugin.getLangString("Error-Messages.Mine.Material-In-Composition"));
                return;
            }

            if (!blockData.getMaterial().isBlock()) {
                player.sendMessage(CataMines.PREFIX + plugin.getLangString("Error-Messages.Mine.Material-Not-Solid"));
                return;
            }

            player.performCommand("cm set " + mine.getName() + " " + blockData.getAsString() + " 0%");
            updateMenus();
            return;
        }

        if (event.getRawSlot() == 49) {
            new MineMenu(playerMenuUtility).open();
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
            return;
        } else if (event.getRawSlot() == 48) {
            if (page == 0) {
                player.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.GUI.First-Page"));
            } else {
                page = page - 1;
                super.open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
            }
            return;
        } else if (event.getRawSlot() == 50) {
            if (!((index + 1) >= materials.size())) {
                page = page + 1;
                super.open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
            } else {
                player.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.GUI.Last-Page"));
            }
            return;
        } else if (event.getRawSlot() == 53) {
            return;
        }

        if (Objects.equals(event.getClickedInventory(), event.getWhoClicked().getInventory())) {
            return;
        }

        BlockData blockData = Bukkit.createBlockData(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getLore().get(0)));

        if (blockData.getMaterial().equals(Material.BONE_MEAL)) {
            blockData = Bukkit.createBlockData(Material.AIR);
        }

        if (event.isRightClick()) {
            player.performCommand("cm unset " + mine.getName() + " " + blockData.getAsString());
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
            updateMenus();
            return;
        }

        CataMineBlock clickedBlock = mine.getBlock(blockData);
        playerMenuUtility.setBlock(clickedBlock);

        new CompositionBlockMenu(playerMenuUtility).open();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
    }

    @Override
    public void setMenuItems() {
        addMenuBorder();

        ArrayList<CataMineBlock> blocks = new ArrayList<>(mine.getBlocks());

        if (!blocks.isEmpty()) {
            for (int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * page + i;
                if (index >= blocks.size()) break;
                if (blocks.get(index) != null) {

                    BlockData blockData = blocks.get(index).getBlockData();
                    Material material = blockData.getMaterial();
                    List<String> finalCompBlockLore = new ArrayList<>();
                    finalCompBlockLore.add(ChatColor.WHITE + blockData.getAsString(true));
                    List<String> compBlockLore = plugin.getLangStringList("GUI.Composition-Menu.Items.Composition-Block.Lore");
                    compBlockLore.forEach(s -> finalCompBlockLore.add(s.replaceAll("%chance%", String.valueOf(blocks.get(index).getChance()))));
                    inventory.addItem(ItemStackBuilder.buildItem(!material.equals(Material.AIR) ? material : Material.BONE_MEAL, "",
                            finalCompBlockLore));

                }
            }
        }

        inventory.setItem(53, ItemStackBuilder.buildItem(Material.SIGN, plugin.getLangString("GUI.Composition-Menu.Items.Info.Name"),
                plugin.getLangStringList("GUI.Composition-Menu.Items.Info.Lore")));
    }
}
