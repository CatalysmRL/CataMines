package me.catalysmrl.legacycatamines.menusystem.menus.minemenus.compositionmenu;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.utils.helper.ItemStackBuilder;
import me.catalysmrl.legacycatamines.menusystem.PaginatedMenu;
import me.catalysmrl.legacycatamines.menusystem.PlayerMenuUtility;
import me.catalysmrl.legacycatamines.menusystem.menus.MineMenu;
import me.catalysmrl.catamines.mine.components.composition.CataMineBlock;
import me.catalysmrl.legacycatamines.mine.mines.CuboidCataMine;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

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
        if (event.getCurrentItem() == null) {
            return;
        }

        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();

        switch (slot) {
            case 49:
                new MineMenu(playerMenuUtility).open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                return;
            case 48:
                if (page == 0) {
                    player.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.GUI.First-Page"));
                } else {
                    page = page - 1;
                    super.open();
                }
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                return;
            case 50:
                if (!((index + 1) >= mine.getBlocks().size())) {
                    page = page + 1;
                    super.open();
                } else {
                    player.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.GUI.Last-Page"));
                }
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                return;
        }

        if (event.getClickedInventory() == player.getOpenInventory().getTopInventory()) {
            if (!(slot >= 10 && slot <= 34) || (slot + 1) % 9 == 0 || slot % 9 == 0) return;

            int row = slot / 9;
            int index = getMaxItemsPerPage() * page + (slot - (8 + 2 * row));
            if (event.isRightClick()) {
                mine.removeBlock(index);
                mine.save();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                updateMenus();
            } else {
                playerMenuUtility.setBlock(mine.getBlocks().get(index));
                new CompositionBlockMenu(playerMenuUtility).open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
            }
            return;
        }

        Material mat = event.getCurrentItem().getType();
        switch (mat) {
            case WATER_BUCKET:
                mat = Material.WATER;
                break;
            case LAVA_BUCKET:
                mat = Material.LAVA;
                break;
        }

        if (!mat.isBlock()) {
            player.sendMessage(plugin.getLangString("Error-Messages.Mine.Material-Not-Solid"));
            return;
        }

        BlockData blockData = mat.createBlockData();
        mine.addBlock(new CataMineBlock(blockData, 0));
        mine.save();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
        updateMenus();
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

                    CataMineBlock cataBlock = blocks.get(index);
                    BlockData blockData = cataBlock.getBlockState();
                    Material material = blockData.getMaterial();
                    if (!material.isItem() || material.equals(Material.AIR)) {
                        material = Material.WRITTEN_BOOK;
                    }

                    List<String> lore = new ArrayList<>();
                    plugin.getLangStringList("GUI.Composition-Menu.Items.Composition-Block.Lore")
                            .forEach(s -> lore.add(s.replaceAll("%blockdata%", blockData.getAsString(true).substring(10 + blockData.getMaterial().name().length()))
                                    .replaceAll("%chance%", String.valueOf(cataBlock.getChance()))));
                    inventory.addItem(ItemStackBuilder.buildItem(material, material == Material.WRITTEN_BOOK ? ChatColor.WHITE + blockData.getMaterial().name() : "", lore));

                }
            }
        }

        inventory.setItem(53, ItemStackBuilder.buildItem(Material.SIGN, plugin.getLangString("GUI.Composition-Menu.Items.Info.Name"),
                plugin.getLangStringList("GUI.Composition-Menu.Items.Info.Lore")));
    }
}
