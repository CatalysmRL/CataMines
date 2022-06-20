package de.c4t4lysm.catamines.utils.menusystem.menus;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.schedulers.MineManager;
import de.c4t4lysm.catamines.utils.ItemStackBuilder;
import de.c4t4lysm.catamines.utils.menusystem.PaginatedMenu;
import de.c4t4lysm.catamines.utils.menusystem.PlayerMenuUtility;
import de.c4t4lysm.catamines.utils.mine.components.CataMineBlock;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MineListMenu extends PaginatedMenu {

    public MineListMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        playerMenuUtility.setMenu(this);
    }

    public MineListMenu(PlayerMenuUtility playerMenuUtility, int page) {
        super(playerMenuUtility);
        playerMenuUtility.setMenu(this);
        this.page = page;
    }

    @Override
    public String getMenuName() {
        return CataMines.getInstance().getLangString("GUI.Mine-List-Menu.Title");
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType().equals(Material.GRAY_STAINED_GLASS_PANE) || Objects.equals(event.getClickedInventory(), event.getWhoClicked().getInventory())) {
            return;
        }

        if (itemStack.getItemMeta() == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        List<CuboidCataMine> cuboidCataMines = MineManager.getInstance().getMines();

        if (event.getCurrentItem().getType() != Material.AIR && !event.getCurrentItem().getType().equals(Material.GRAY_STAINED_GLASS_PANE)) {

            switch (event.getRawSlot()) {
                case 48:
                    if (page == 0) {
                        player.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.GUI.First-Page"));
                    } else {
                        page = page - 1;
                        playerMenuUtility.setMineListMenuPage(playerMenuUtility.getMineListMenuPage() - 1);
                        super.open();
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                    }
                    break;
                case 49:
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                    break;
                case 50:
                    if (!((index + 1) >= cuboidCataMines.size())) {
                        page = page + 1;
                        playerMenuUtility.setMineListMenuPage(playerMenuUtility.getMineListMenuPage() + 1);
                        super.open();
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                    } else {
                        player.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.GUI.Last-Page"));
                    }
                    break;
                default:
                    if (MineManager.getInstance().getMine(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName())) != null) {
                        new MineMenu(playerMenuUtility, MineManager.getInstance().getMine(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()))).open();
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3F, 1F);
                    } else {
                        player.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.GUI.Mine-Doesnt-Exist"));
                    }
            }

        }

    }

    @Override
    public void setMenuItems() {
        addMenuBorder();

        List<CuboidCataMine> cuboidCataMines = MineManager.getInstance().getMines();

        if (cuboidCataMines != null && !cuboidCataMines.isEmpty()) {
            for (int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * page + i;
                if (index >= cuboidCataMines.size()) break;
                if (cuboidCataMines.get(index) != null) {
                    CuboidCataMine cuboidCataMine = cuboidCataMines.get(index);

                    Material material = Material.STICK;
                    CataMineBlock cataBlock;
                    if (!cuboidCataMine.getBlocks().isEmpty()) {
                        cataBlock = cuboidCataMine.getBlocks().stream().max(Comparator.comparingDouble(CataMineBlock::getChance)).get();
                        material = cataBlock.getBlockData().getMaterial();
                    }

                    if (!material.isSolid()) {
                        material = Material.WRITTEN_BOOK;
                    }

                    ArrayList<String> lore = new ArrayList<>();
                    lore.add("");
                    lore.add(ChatColor.AQUA + "Composition:");
                    int miniIndex = 1;
                    for (CataMineBlock block : cuboidCataMine.getBlocks()) {
                        lore.add(ChatColor.RED + "  " + miniIndex + ". " + ChatColor.GOLD + block.getBlockData().getMaterial() + ": " + ChatColor.RED + block.getChance() + "%");
                        miniIndex++;
                    }
                    lore.add(ChatColor.AQUA + "Delay: " + ChatColor.RED + cuboidCataMine.getResetDelay());
                    lore.add(ChatColor.AQUA + "Reset percentage: " + ChatColor.RED + cuboidCataMine.getResetPercentage() + "%");
                    lore.add(ChatColor.AQUA + "Replace mode: " + ChatColor.RED + cuboidCataMine.isReplaceMode());
                    lore.add(ChatColor.AQUA + "Warns: " + ChatColor.RED + cuboidCataMine.isWarn());
                    lore.add(ChatColor.AQUA + "  Warns hotbar: " + ChatColor.RED + cuboidCataMine.isWarnHotbar());
                    lore.add(ChatColor.AQUA + "  Warns globally: " + ChatColor.RED + cuboidCataMine.isWarnGlobal());
                    String warnSeconds = cuboidCataMine.getWarnSeconds().toString();
                    lore.add(ChatColor.AQUA + "  Warn seconds: " + ChatColor.RED + warnSeconds.substring(1, warnSeconds.length() - 1));
                    lore.add(ChatColor.AQUA + "  Warn distance: " + ChatColor.RED + cuboidCataMine.getWarnDistance());
                    lore.add(ChatColor.AQUA + "Is stopped: " + ChatColor.RED + cuboidCataMine.isStopped());

                    ItemStack mineItem = ItemStackBuilder.buildItem(material, !cuboidCataMine.isStopped() && cuboidCataMine.isRunnable() ? ChatColor.GREEN + cuboidCataMine.getName() : ChatColor.RED + cuboidCataMine.getName(), lore);

                    inventory.addItem(mineItem);
                }
            }
        }
    }
}
