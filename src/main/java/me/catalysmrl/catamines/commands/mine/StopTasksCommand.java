package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.AbstractCommand;
import me.catalysmrl.catamines.command.utils.CommandContext;
import me.catalysmrl.catamines.command.utils.CommandException;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Messages;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

public class StopTasksCommand extends AbstractCommand {

    public StopTasksCommand() {
        super("stoptasks", "catamines.stoptasks", Predicates.any(), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx) throws CommandException {
        plugin.getMineManager().shutDown();
        Messages.sendPrefixed(sender, "&cMine tasks stopped.");
    }

    @Override
    public Message getDescription() {
        return Message.STOPTASKS_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.STOPTASKS_USAGE;
    }
}
