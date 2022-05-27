package de.c4t4lysm.catamines.utils.menusystem.menus.minemenus.compositionmenu;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.utils.ItemStackBuilder;
import de.c4t4lysm.catamines.utils.menusystem.PaginatedMenu;
import de.c4t4lysm.catamines.utils.menusystem.PlayerMenuUtility;
import de.c4t4lysm.catamines.utils.menusystem.menus.MineMenu;
import de.c4t4lysm.catamines.utils.mine.components.CataMineBlock;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompositionMenu extends PaginatedMenu {

    private final CuboidCataMine cuboidCataMine;
    private final CataMines plugin;

    public CompositionMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        playerMenuUtility.setMenu(this);
        cuboidCataMine = playerMenuUtility.getMine();
        plugin = CataMines.getInstance();
    }

    @Override
    public String getMenuName() {
        return plugin.getLangString("GUI.Composition-Menu.Title").replaceAll("%chance%", String.valueOf(cuboidCataMine.getCompositionChance()));
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
        cuboidCataMine.getBlocks().forEach(block -> materials.add(block.getMaterial()));

        Player player = (Player) event.getWhoClicked();
        if (Objects.equals(event.getClickedInventory(), player.getInventory())) {

            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem.getType().equals(Material.BONE_MEAL)) {
                clickedItem.setType(Material.AIR);
            }

            if (materials.contains(clickedItem.getType())) {
                player.sendMessage(CataMines.PREFIX + plugin.getLangString("Error-Messages.Mine.Material-In-Composition"));
                return;
            }

            if (!clickedItem.getType().isBlock()) {
                player.sendMessage(CataMines.PREFIX + plugin.getLangString("Error-Messages.Mine.Material-Not-Solid"));
                return;
            }

            player.performCommand("cm set " + cuboidCataMine.getName() + " " + clickedItem.getType().name() + " 0%");
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

        Material material = event.getCurrentItem().getType();

        if (material.equals(Material.BONE_MEAL)) {
            material = Material.AIR;
        }

        if (event.isRightClick()) {
            player.performCommand("cm unset " + cuboidCataMine.getName() + " " + material.name());
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
            updateMenus();
            return;
        }

        CataMineBlock clickedBlock = cuboidCataMine.getBlock(material);
        playerMenuUtility.setBlock(clickedBlock);

        new CompositionBlockMenu(playerMenuUtility).open();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
    }

    @Override
    public void setMenuItems() {
        addMenuBorder();

        ArrayList<CataMineBlock> blocks = new ArrayList<>(cuboidCataMine.getBlocks());

        if (!blocks.isEmpty()) {
            for (int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * page + i;
                if (index >= blocks.size()) break;
                if (blocks.get(index) != null) {

                    Material material = blocks.get(index).getMaterial();
                    List<String> compBlockLore = plugin.getLangStringList("GUI.Composition-Menu.Items.Composition-Block.Lore");
                    compBlockLore.replaceAll(s -> s.replaceAll("%chance%", String.valueOf(blocks.get(index).getChance())));
                    inventory.addItem(ItemStackBuilder.buildItem(!material.equals(Material.AIR) ? material : Material.BONE_MEAL, "",
                            compBlockLore));

                }
            }
        }

        inventory.setItem(53, ItemStackBuilder.buildItem(Material.SIGN, plugin.getLangString("GUI.Composition-Menu.Items.Info.Name"),
                plugin.getLangStringList("GUI.Composition-Menu.Items.Info.Lore")));
    }
}
