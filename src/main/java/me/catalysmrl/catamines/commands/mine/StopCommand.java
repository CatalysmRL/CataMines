package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.Flag;
import me.catalysmrl.catamines.api.mine.PropertyHolder;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.command.utils.CommandContext;
import me.catalysmrl.catamines.command.utils.CommandException;
import me.catalysmrl.catamines.command.utils.MineTarget;
import me.catalysmrl.catamines.mine.components.manager.choice.Identifiable;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Messages;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

public class StopCommand extends AbstractMineCommand {

    public StopCommand() {
        super("stop", "catamines.stop", Predicates.any(), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target)
            throws CommandException {
        PropertyHolder holder = (PropertyHolder) target.getTarget();
        holder.setFlag(Flag.STOPPED, true);

        String targetName = target.getMine().getName();
        if (holder instanceof Identifiable) {
            targetName = ((Identifiable) holder).getName();
        }

        Messages.sendPrefixed(sender, "&aStopped: " + targetName);
        requireSave();
    }

    @Override
    public Message getDescription() {
        return Message.STOP_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.STOP_USAGE;
    }
}
