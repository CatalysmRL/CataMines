package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.command.utils.CommandContext;
import me.catalysmrl.catamines.command.utils.CommandException;
import me.catalysmrl.catamines.command.utils.MineTarget;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Messages;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class TeleportPlayersCommand extends AbstractMineCommand {

    public TeleportPlayersCommand() {
        super("teleportplayers", "catamines.teleportplayers", Predicates.equals(2), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx,
            me.catalysmrl.catamines.command.utils.MineTarget target)
            throws CommandException {
        String valueStr = ctx.next();
        if (!valueStr.equalsIgnoreCase("true") && !valueStr.equalsIgnoreCase("false")) {
            Messages.sendPrefixed(sender, "Invalid boolean: " + valueStr);
            return;
        }

        boolean value = Boolean.parseBoolean(valueStr);
        me.catalysmrl.catamines.api.mine.PropertyHolder holder = (me.catalysmrl.catamines.api.mine.PropertyHolder) target
                .getTarget();
        holder.setFlag(me.catalysmrl.catamines.api.mine.Flag.TELEPORT_PLAYERS, value);

        String targetName = target.getMine().getName();
        if (holder instanceof me.catalysmrl.catamines.mine.components.manager.choice.Identifiable) {
            targetName = ((me.catalysmrl.catamines.mine.components.manager.choice.Identifiable) holder).getName();
        }

        Messages.sendPrefixed(sender, "Teleport players set to " + value + " for " + targetName);
        requireSave();
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target) {
        if (ctx.remaining() == 1) {
            return Arrays.asList("true", "false");
        }
        return super.tabComplete(plugin, sender, ctx, target);
    }

    @Override
    public Message getDescription() {
        return Message.TELEPORTPLAYERS_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.TELEPORTPLAYERS_USAGE;
    }
}
