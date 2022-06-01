package de.c4t4lysm.catamines.utils.mine.placeholders;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.schedulers.MineManager;
import de.c4t4lysm.catamines.utils.Utils;
import de.c4t4lysm.catamines.utils.mine.AbstractCataMine;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class CataMinePlaceHolders extends PlaceholderExpansion {

    private final CataMines plugin;

    public CataMinePlaceHolders(CataMines plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "catamines";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    public boolean canRegister() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        int mineIndex = 1;

        if (identifier.equals("prefix")) {
            return plugin.getConfig().getString("prefix", "&6[&bCata&aMines&6] &7");
        } else if (identifier.startsWith("countdown")) {
            String[] strs = identifier.split("_");
            AbstractCataMine cataMine = MineManager.getInstance().getMine(strs[mineIndex]);
            if (cataMine != null) {
                return cataMine.checkRunnable() ? Utils.secondsToTimeFormat(cataMine.getCountdown()) : "inactive";
            }
            return plugin.getLangString("Error-Messages.Mine.Not-Exist");
        } else if (identifier.startsWith("remainingseconds")) {
            String[] strs = identifier.split("_");
            AbstractCataMine cataMine = MineManager.getInstance().getMine(strs[mineIndex]);
            if (cataMine != null) {
                return cataMine.checkRunnable() ? String.valueOf(cataMine.getCountdown()) : "inactive";
            }

            return plugin.getLangString("Error-Messages.Mine.Not-Exist");
        } else if (identifier.startsWith("totalblocks")) {
            String[] strs = identifier.split("_");
            AbstractCataMine cataMine = MineManager.getInstance().getMine(strs[mineIndex]);
            if (cataMine != null) {
                return String.valueOf(cataMine.getTotalBlocks());
            }
            return plugin.getLangString("Error-Messages.Mine.Not-Exist");
        } else if (identifier.startsWith("minedblocks")) {
            String[] strs = identifier.split("_");
            AbstractCataMine cataMine = MineManager.getInstance().getMine(strs[mineIndex]);
            if (cataMine != null) {
                return String.valueOf(cataMine.getMinedBlocks());
            }
            return plugin.getLangString("Error-Messages.Mine.Not-Exist");
        } else if (identifier.startsWith("remainingblocks")) {
            String[] strs = identifier.split("_");
            AbstractCataMine cataMine = MineManager.getInstance().getMine(strs[mineIndex]);
            if (cataMine != null) {
                return String.valueOf(cataMine.getRemainingBlocks());
            }
            return plugin.getLangString("Error-Messages.Mine.Not-Exist");
        } else if (identifier.startsWith("remainingblockspercentage")){
            String[] strs = identifier.split("_");
            AbstractCataMine cataMine = MineManager.getInstance().getMine(strs[mineIndex]);
            if (cataMine != null) {
                return String.valueOf(cataMine.getRemainingBlocksPer());
            }
            return plugin.getLangString("Error-Messages.Mine.Not-Exist");
        } else if (identifier.startsWith("resetpercentage")) {
            String[] strs = identifier.split("_");
            AbstractCataMine cataMine = MineManager.getInstance().getMine(strs[mineIndex]);
            if (cataMine != null) {
                return String.valueOf(cataMine.getResetPercentage());
            }
            return plugin.getLangString("Error-Messages.Mine.Not-Exist");
        }

        return null;
    }
}
