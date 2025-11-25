package me.catalysmrl.catamines.commands.mine.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Messages;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class WarnCommand extends AbstractMineCommand {

    public WarnCommand() {
        super("warn", "catamines.warn", Predicates.inRange(2, 3), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine) throws CommandException {
        String subCommand = ctx.next().toLowerCase();
        
        // Legacy: /cm warn <mine> true/false -> enable/disable warning
        // Refactored: /cm warn <mine> <setting> <value>
        // Settings: enable, distance, global, hotbar, seconds
        
        // Handle boolean toggle for "warn" directly if 2nd arg is boolean?
        // Legacy was /cm warn <mine> <true/false>
        if (subCommand.equals("true") || subCommand.equals("false")) {
            boolean value = Boolean.parseBoolean(subCommand);
            mine.setWarn(value);
            Messages.sendPrefixed(sender, "Warn enabled set to " + value + " for mine " + mine.getName());
            requireSave();
            return;
        }

        if (!ctx.hasNext()) {
            Messages.sendPrefixed(sender, "Usage: /cm warn <mine> <setting> <value>");
            Messages.sendPrefixed(sender, "Settings: enable, distance, global, hotbar, seconds");
            return;
        }

        String valueStr = ctx.next();

        switch (subCommand) {
            case "enable":
                boolean enable = Boolean.parseBoolean(valueStr);
                mine.setWarn(enable);
                Messages.sendPrefixed(sender, "Warn enabled set to " + enable + " for mine " + mine.getName());
                requireSave();
                break;
            case "distance":
                try {
                    int distance = Integer.parseInt(valueStr);
                    mine.setWarnDistance(distance);
                    Messages.sendPrefixed(sender, "Warn distance set to " + distance + " for mine " + mine.getName());
                    requireSave();
                } catch (NumberFormatException e) {
                    Messages.sendPrefixed(sender, "Invalid number: " + valueStr);
                }
                break;
            case "global":
                boolean global = Boolean.parseBoolean(valueStr);
                mine.setWarnGlobal(global);
                Messages.sendPrefixed(sender, "Warn global set to " + global + " for mine " + mine.getName());
                requireSave();
                break;
            case "hotbar":
                boolean hotbar = Boolean.parseBoolean(valueStr);
                mine.setWarnHotbar(hotbar);
                Messages.sendPrefixed(sender, "Warn hotbar set to " + hotbar + " for mine " + mine.getName());
                break;
            case "seconds":
                try {
                    int seconds = Integer.parseInt(valueStr);
                    mine.setWarnSeconds(seconds);
                    Messages.sendPrefixed(sender, "Warn seconds set to " + seconds + " for mine " + mine.getName());
                    requireSave();
                } catch (NumberFormatException e) {
                    Messages.sendPrefixed(sender, "Invalid number: " + valueStr);
                }
                break;
            default:
                Messages.sendPrefixed(sender, "Unknown setting: " + subCommand);
                return;
        }
        requireSave();
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine) {
        if (ctx.remaining() == 1) {
            return Arrays.asList("enable", "distance", "global", "hotbar", "seconds", "true", "false");
        }
        return super.tabComplete(plugin, sender, ctx, mine);
    }

    @Override
    public String getDescription() {
        return "Configures warning settings for a mine";
    }

    @Override
    public String getUsage() {
        return "/cm warn <mine> <setting> <value>";
    }
}
