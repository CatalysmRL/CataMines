package me.catalysmrl.legacycatamines.utils;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector2;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.legacycatamines.schedulers.MineManager;
import me.catalysmrl.legacycatamines.menusystem.Menu;
import me.catalysmrl.legacycatamines.menusystem.PlayerMenuUtility;
import me.catalysmrl.legacycatamines.menusystem.menus.minemenus.compositionmenu.CompositionBlockMenu;
import me.catalysmrl.legacycatamines.menusystem.menus.minemenus.compositionmenu.CompositionMenu;
import me.catalysmrl.legacycatamines.menusystem.menus.minemenus.compositionmenu.blockloottable.ChangeBlockLootTableMenu;
import me.catalysmrl.legacycatamines.menusystem.menus.minemenus.compositionmenu.blockloottable.LootItemListMenu;
import me.catalysmrl.legacycatamines.menusystem.menus.minemenus.compositionmenu.blockloottable.LootItemMenu;
import me.catalysmrl.legacycatamines.mine.AbstractCataMine;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

    public static String setPlaceholders(String input, AbstractCataMine mine) {

        Map<String, String> replacements = new HashMap<>();
        replacements.put("cm", StringUtils.chop(CataMines.PREFIX));
        replacements.put("mine", mine.getName());
        replacements.put("seconds", String.valueOf(mine.getCountdown()));
        replacements.put("formattedseconds", secondsToTimeFormat(mine.getCountdown()));
        replacements.put("formattedtime", mine.getFormattedTimeString());
        replacements.put("time", mine.getCountdown() / 60 == 1 ?
                CataMines.getInstance().getLangString("Time.Second") :
                CataMines.getInstance().getLangString("Time.Seconds"));
        replacements.put("resetpercentage", String.valueOf(mine.getResetPercentage()));
        replacements.put("remainingblocksper", String.valueOf(mine.getRemainingBlocksPer()));

        StrSubstitutor strSubstitutor = new StrSubstitutor(replacements, "%", "%");
        String translatedText = strSubstitutor.replace(input);

        if (CataMines.getInstance().placeholderAPI) {
            translatedText = PlaceholderAPI.setPlaceholders(null, translatedText);
        }

        return ChatColor.translateAlternateColorCodes('&', translatedText);
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

    public static BlockVector3 unparseBlockVector3(String parsedVector) {
        String[] args = parsedVector.split(",");
        try {
            return BlockVector3.at(Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Vector3 unparseVector3(String parsedVector) {
        String[] args = parsedVector.split(",");
        try {
            return Vector3.at(Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Vector2 unparseVector2(String parsedVector) {
        String[] args = parsedVector.split(",");
        try {
            return Vector2.at(Double.parseDouble(args[0]), Double.parseDouble(args[1]));
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        return null;
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


    public static List<Location> getRegionVertices(Player player) {
        return CataMines.getInstance().getWorldEditPlugin().getSession(player).getRegionSelector(BukkitAdapter.adapt(player.getWorld())).getVertices()
                .stream().map(blockVector3 -> BukkitAdapter.adapt(player.getWorld(), blockVector3)).collect(Collectors.toList());
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

            if (playerMenuUtility.getMenu() instanceof CompositionBlockMenu ||
                    playerMenuUtility.getMenu() instanceof ChangeBlockLootTableMenu ||
                    playerMenuUtility.getMenu() instanceof LootItemListMenu ||
                    playerMenuUtility.getMenu() instanceof LootItemMenu) {

                if (!playerMenuUtility.getMine().containsBlock(playerMenuUtility.getBlock())) {
                    playerMenuUtility.setBlock(null);
                    new CompositionMenu(playerMenuUtility);
                    player.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.GUI.Block-Deleted"));
                }
            }

            if (playerMenuUtility.getMenu() instanceof LootItemMenu) {
                if (!playerMenuUtility.getBlock().getDrops().contains(playerMenuUtility.getItem())) {
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
