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

public class TeleportPlayersCommand extends AbstractMineCommand {

    public TeleportPlayersCommand() {
        super("teleportplayers", "catamines.teleportplayers", Predicates.equals(2), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine) throws CommandException {
        String valueStr = ctx.next();
        if (!valueStr.equalsIgnoreCase("true") && !valueStr.equalsIgnoreCase("false")) {
            Messages.sendPrefixed(sender, "Invalid boolean: " + valueStr);
            return;
        }
        
        boolean value = Boolean.parseBoolean(valueStr);
        mine.setTeleportPlayers(value);
        Messages.sendPrefixed(sender, "Teleport players set to " + value + " for mine " + mine.getName());
        requireSave();
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine) {
        if (ctx.remaining() == 1) {
            return Arrays.asList("true", "false");
        }
        return super.tabComplete(plugin, sender, ctx, mine);
    }

    @Override
    public String getDescription() {
        return "Sets whether players should be teleported when mine resets";
    }

    @Override
    public String getUsage() {
        return "/cm teleportplayers <mine> <true/false>";
    }
}
