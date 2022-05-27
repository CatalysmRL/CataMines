package de.c4t4lysm.catamines.utils.menusystem;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.utils.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class PaginatedMenu extends Menu {

    protected int page = 0;
    protected int maxItemsPerPage = 21;
    protected int index = 0;

    public PaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    public void addMenuBorder() {

        CataMines plugin = CataMines.getInstance();

        ItemStack fillerGlass = ItemStackBuilder.buildItem(Material.GRAY_STAINED_GLASS_PANE, " ");

        inventory.setItem(48, ItemStackBuilder.buildItem(Material.ARROW, plugin.getLangString("GUI.Universal.Previous-Page.Name"), plugin.getLangStringList("GUI.Universal.Previous-Page.Lore")));

        inventory.setItem(49, ItemStackBuilder.buildItem(Material.BARRIER, plugin.getLangString("GUI.Universal.Close.Name"), plugin.getLangStringList("GUI.Universal.Close.Lore")));

        inventory.setItem(50, ItemStackBuilder.buildItem(Material.ARROW, plugin.getLangString("GUI.Universal.Next-Page.Name"), plugin.getLangStringList("GUI.Universal.Next-Page.Lore")));

        for (int i = 0; i < 10; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, fillerGlass.clone());
            }
        }

        inventory.setItem(17, fillerGlass.clone());
        inventory.setItem(18, fillerGlass.clone());
        inventory.setItem(26, fillerGlass.clone());
        inventory.setItem(27, fillerGlass.clone());
        inventory.setItem(35, fillerGlass.clone());
        inventory.setItem(36, fillerGlass.clone());

        for (int i = 44; i < 54; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, fillerGlass.clone());
            }
        }

    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }


}
