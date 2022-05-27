package de.c4t4lysm.catamines.utils.menusystem.menus.minemenus.compositionmenu.blockloottable;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.utils.ItemStackBuilder;
import de.c4t4lysm.catamines.utils.menusystem.Menu;
import de.c4t4lysm.catamines.utils.menusystem.PlayerMenuUtility;
import de.c4t4lysm.catamines.utils.menusystem.menus.minemenus.compositionmenu.CompositionBlockMenu;
import de.c4t4lysm.catamines.utils.mine.components.CataMineBlock;
import de.c4t4lysm.catamines.utils.mine.components.CataMineLootItem;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ChangeBlockLootTableMenu extends Menu {

    private final CuboidCataMine mine;
    private final CataMineBlock block;

    public ChangeBlockLootTableMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        this.mine = playerMenuUtility.getMine();
        this.block = playerMenuUtility.getBlock();
    }

    @Override
    public String getMenuName() {
        return CataMines.getInstance().getLangString("GUI.Change-Loot-Table-Menu.Title");
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getRawSlot() >= 45 && event.getRawSlot() <= 53) {
            event.setCancelled(true);
            if (event.getRawSlot() == 45) {
                new CompositionBlockMenu(playerMenuUtility).open();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                return;
            }

            if (event.getRawSlot() == 53) {
                block.getLootTable().clear();
                for (int i = 0; i < 45; i++) {
                    if (event.getInventory().getItem(i) == null) {
                        continue;
                    }
                    ItemStack itemStack = event.getInventory().getItem(i);
                    block.getLootTable().add(new CataMineLootItem(itemStack));
                }

                mine.save();
                event.getWhoClicked().sendMessage(CataMines.getInstance().getLangString("GUI.Change-Loot-Table-Menu.Items.Save-Table.Message"));
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                updateMenus();
            }
        }
    }

    @Override
    public void setMenuItems() {

        for (int i = 0; i < block.getLootTable().size(); i++) {
            if (i >= 45) {
                break;
            }
            inventory.setItem(i, block.getLootTable().get(i).getItem());
        }

        for (int i = 45; i < getSlots(); i++) {
            inventory.setItem(i, ItemStackBuilder.buildItem(Material.GRAY_STAINED_GLASS_PANE, " "));
        }

        inventory.setItem(45, ItemStackBuilder.buildItem(Material.ARROW, CataMines.getInstance().getLangString("GUI.Universal.Back-To-Block-Menu.Name"), CataMines.getInstance().getLangStringList("GUI.Universal.Back-To-Block-Menu.Lore")));

        inventory.setItem(53, ItemStackBuilder.buildItem(Material.LIME_STAINED_GLASS, CataMines.getInstance().getLangString("GUI.Change-Loot-Table-Menu.Items.Save-Table.Name")));
    }
}
