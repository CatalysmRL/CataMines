package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.Flag;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.command.utils.CommandContext;
import me.catalysmrl.catamines.command.utils.CommandException;
import me.catalysmrl.catamines.command.utils.MineTarget;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.message.Messages;

import org.bukkit.command.CommandSender;

public class UnsetCommand extends AbstractMineCommand {
    public UnsetCommand() {
        super("unset", "catamines.unset", Predicates.equals(1), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target)
            throws CommandException {
        assertArgLength(ctx);
        String flagName = ctx.next();
        Flag<?> flag = Flag.getByName(flagName);

        if (flag == null) {
            Messages.sendPrefixed(sender, "&cUnknown flag: " + flagName);
            return;
        }

        ((me.catalysmrl.catamines.api.mine.PropertyHolder) target.getTarget()).setFlag(flag, null);
        Messages.sendPrefixed(sender, "&aUnset flag " + flag.getName() + " for target.");
        requireSave();
    }

    @Override
    public Message getDescription() {
        return Message.UNSET_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.UNSET_USAGE;
    }
}
