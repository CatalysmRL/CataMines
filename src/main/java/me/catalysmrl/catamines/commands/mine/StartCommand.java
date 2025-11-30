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
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

public class StartCommand extends AbstractMineCommand {

    public StartCommand() {
        super("start", "catamines.start", Predicates.any(), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target)
            throws CommandException {
        PropertyHolder holder = (PropertyHolder) target.getTarget();
        holder.setFlag(Flag.STOPPED, false);

        String targetName = target.getMine().getName();
        if (holder instanceof Identifiable) {
            targetName = ((Identifiable) holder).getName();
        }

        Message.START_SUCCESS.send(sender, targetName);
        requireSave();
    }

    @Override
    public Message getDescription() {
        return Message.START_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.START_USAGE;
    }
}
