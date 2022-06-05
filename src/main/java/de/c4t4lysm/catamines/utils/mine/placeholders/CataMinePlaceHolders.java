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

        String[] args = identifier.split("_");
        if (args.length <= 1) {
            if (args[0].equalsIgnoreCase("prefix")) {
                return plugin.getConfig().getString("prefix", "&6[&bCata&aMines&6] &7");
            } else {
                return null;
            }
        }

        AbstractCataMine cataMine = MineManager.getInstance().getMine(args[mineIndex]);
        if (cataMine == null) {
            return plugin.getLangString("Error-Messages.Mine.Not-Exist");
        }

        if (!cataMine.isRunnable()) {
            return "inactive";
        }

        switch (args[0].toLowerCase()) {
            case "countdown":
            case "time":
                return Utils.secondsToTimeFormat(cataMine.getCountdown());
            case "remainingseconds":
                return String.valueOf(cataMine.getCountdown());
            case "totalblocks":
                return String.valueOf(cataMine.getTotalBlocks());
            case "remainingblocks":
                return String.valueOf(cataMine.getBlockCount());
            case "minedblocks":
                return String.valueOf(cataMine.getMinedBlocks());
            case "remainingblockspercentage":
            case "remainingblocksper":
                return String.valueOf(cataMine.getRemainingBlocksPer());
            case "resetpercentage":
                return String.valueOf(cataMine.getResetPercentage());
        }

        return null;
    }
}
