package me.catalysmrl.catamines.mine.placeholders;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.message.Messages;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.regex.Pattern;

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
        String delimiter = "_";
        String regex = "(?<!\\\\)(?:" + Pattern.quote(delimiter) + "|\\\\(?!\\\\))";
        String[] args = identifier.split(regex);

        if (args.length <= 1) {
            return switch (args[0]) {
                case "prefix", "p" -> Messages.PREFIX;
                default -> null;
            };
        }

        Optional<CataMine> mineOptional = plugin.getMineManager().getMine(args[mineIndex]);
        if (mineOptional.isEmpty()) {
            return Message.MINE_NOT_EXIST.getMessage();
        }

        CataMine mine = mineOptional.get();

        // TODO: Placholders via new CataMine interface
        /*
        return switch (args[0].toLowerCase()) {
            case "countdown", "time" -> Utils.secondsToTimeFormat(mine.getCountdown());
            case "remainingseconds" -> String.valueOf(mine.getCountdown());
            case "totalblocks" -> String.valueOf(mine.getTotalBlocks());
            case "remainingblocks" -> String.valueOf(mine.getBlockCount());
            case "minedblocks" -> String.valueOf(mine.getMinedBlocks());
            case "remainingblockspercentage", "remainingblocksper" -> String.valueOf(mine.getRemainingBlocksPer());
            case "resetpercentage" -> String.valueOf(mine.getResetPercentage());
            case "timestring" -> mine.getFormattedTimeString();
            default -> null;
        };
         */

        return "Not yet implemented";
    }
}
