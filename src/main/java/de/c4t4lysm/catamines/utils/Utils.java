package de.c4t4lysm.catamines.utils;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.schedulers.MineManager;
import de.c4t4lysm.catamines.utils.menusystem.Menu;
import de.c4t4lysm.catamines.utils.menusystem.PlayerMenuUtility;
import de.c4t4lysm.catamines.utils.menusystem.menus.minemenus.compositionmenu.CompositionBlockMenu;
import de.c4t4lysm.catamines.utils.menusystem.menus.minemenus.compositionmenu.CompositionMenu;
import de.c4t4lysm.catamines.utils.menusystem.menus.minemenus.compositionmenu.blockloottable.ChangeBlockLootTableMenu;
import de.c4t4lysm.catamines.utils.menusystem.menus.minemenus.compositionmenu.blockloottable.LootItemListMenu;
import de.c4t4lysm.catamines.utils.menusystem.menus.minemenus.compositionmenu.blockloottable.LootItemMenu;
import de.c4t4lysm.catamines.utils.mine.AbstractCataMine;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class Utils {

    public static String setPlaceholders(String input, AbstractCataMine mine) {
        String translatedText = input
                .replaceAll("%cm%", StringUtils.chop(CataMines.PREFIX))
                .replaceAll("%mine%", mine.getName())
                .replaceAll("%seconds%", String.valueOf(mine.getCountdown()))
                .replaceAll("%formattedseconds%", secondsToTimeFormat(mine.getCountdown()))
                .replaceAll("%formattedtime%", mine.getFormattedTimeString())
                .replaceAll("%time%", mine.getCountdown() / 60 == 1 ?
                        CataMines.getInstance().getLangString("Time.Second") :
                        CataMines.getInstance().getLangString("Time.Seconds"))
                .replaceAll("%resetpercentage%", String.valueOf(mine.getResetPercentage()))
                .replaceAll("%remainingblocksper%", String.valueOf(mine.getRemainingBlocksPer()));
        if (CataMines.getInstance().placeholderAPI) {
            translatedText = PlaceholderAPI.setPlaceholders(null, translatedText);
        } else {
            translatedText = ChatColor.translateAlternateColorCodes('&', translatedText);
        }

        return translatedText;
    }

    public static String setPlaceholdersAfterEvent(String input, AbstractCataMine mine) {
        String translatedText = input
                .replaceAll("%cm%", StringUtils.chop(CataMines.PREFIX))
                .replaceAll("%mine%", mine.getName())
                .replaceAll("%seconds%", String.valueOf(mine.getCountdown()))
                .replaceAll("%formattedseconds%", secondsToTimeFormat(mine.getCountdown()))
                .replaceAll("%formattedtime%", mine.getFormattedTimeString())
                .replaceAll("%time%", mine.getCountdown() / 60 == 1 ?
                        CataMines.getInstance().getLangString("Time.Second") :
                        CataMines.getInstance().getLangString("Time.Seconds"))
                .replaceAll("%resetpercentage%", String.valueOf(mine.getResetPercentage()))
                .replaceAll("%remainingblocksper%", String.valueOf(Math.round(((double) (mine.getRemainingBlocks() - 1) / (double) mine.getTotalBlocks()) * 10000d) / 100d));
        if (CataMines.getInstance().placeholderAPI) {
            translatedText = PlaceholderAPI.setPlaceholders(null, translatedText);
        } else {
            translatedText = ChatColor.translateAlternateColorCodes('&', translatedText);
        }

        return translatedText;
    }

    public static String secondsToTimeFormat(int seconds) {
        if (seconds >= 60) {
            if (seconds >= 3600) {
                return String.format("%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
            }
            return String.format("%d:%02d", seconds / 60, seconds % 60);
        }
        return String.valueOf(seconds);
    }

    @Nullable
    public static String regionToStr(@Nonnull Region region) {
        String reg = "";

        if (region.getWorld() == null) return null;
        reg += region.getWorld().getName() + ";";
        reg += region.getMinimumPoint().getX() + ";";
        reg += region.getMinimumPoint().getY() + ";";
        reg += region.getMinimumPoint().getZ() + ";";
        reg += region.getMaximumPoint().getX() + ";";
        reg += region.getMaximumPoint().getY() + ";";
        reg += region.getMaximumPoint().getZ() + ";";

        return reg;
    }

    @Nonnull
    public static String[] regionToArray(@Nonnull Region region) {
        String[] strings = new String[7];

        strings[0] = region.getWorld().getName();
        BlockVector3 minP = region.getMinimumPoint();
        BlockVector3 maxP = region.getMaximumPoint();
        strings[1] = String.valueOf(minP.getX());
        strings[2] = String.valueOf(minP.getY());
        strings[3] = String.valueOf(minP.getZ());
        strings[4] = String.valueOf(maxP.getX());
        strings[5] = String.valueOf(maxP.getY());
        strings[6] = String.valueOf(maxP.getZ());

        return strings;
    }

    @Nullable
    public static Region strToRegion(@Nonnull String str) {
        String[] args = str.split(";");

        if (Bukkit.getWorld(args[0]) == null) return null;

        return new CuboidRegion(BukkitAdapter.adapt(Bukkit.getWorld(args[0])), BlockVector3.at(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])),
                BlockVector3.at(Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6])));
    }

    @Nullable
    public static Region getWorldEditSelectionOfPlayer(WorldEditPlugin worldEditPlugin, Player player) {
        Region selection;

        try {
            if (worldEditPlugin != null) {
                selection = worldEditPlugin.getSession(player).getSelection(BukkitAdapter.adapt(player.getWorld()));
                return selection;
            }
        } catch (IncompleteRegionException ex) {
            player.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Mine.Incomplete-Region"));
        }
        return null;
    }

    public static void updateMenus() {

        for (Map.Entry<Player, PlayerMenuUtility> entry : CataMines.getPlayerMenuUtilityMap().entrySet()) {
            Player player = entry.getKey();
            PlayerMenuUtility playerMenuUtility = entry.getValue();

            if (playerMenuUtility.getMine() != null && !MineManager.getInstance().getMines().contains(playerMenuUtility.getMine())) {
                player.closeInventory();
                CataMines.removePlayerMenuUtility(player);
                player.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.GUI.Mine-Deleted"));
                return;
            }

            if (playerMenuUtility.getMenu() instanceof CompositionBlockMenu
                    || playerMenuUtility.getMenu() instanceof ChangeBlockLootTableMenu ||
                    playerMenuUtility.getMenu() instanceof LootItemListMenu ||
                    playerMenuUtility.getMenu() instanceof LootItemMenu) {

                if (!playerMenuUtility.getMine().containsBlock(playerMenuUtility.getBlock())) {
                    playerMenuUtility.setBlock(null);
                    new CompositionMenu(playerMenuUtility);
                    player.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.GUI.Block-Deleted"));
                }
            }

            if (playerMenuUtility.getMenu() instanceof LootItemMenu) {
                if (!playerMenuUtility.getBlock().getLootTable().contains(playerMenuUtility.getItem())) {
                    playerMenuUtility.setItem(null);
                    new LootItemListMenu(playerMenuUtility);
                    player.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.GUI.Loot-Table-Change"));
                }
            }

            InventoryHolder inventoryHolder = player.getOpenInventory().getTopInventory().getHolder();

            if (inventoryHolder instanceof Menu) {
                playerMenuUtility.getMenu().open();
            }

        }

    }
}
