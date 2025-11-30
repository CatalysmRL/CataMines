package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.AbstractCommand;
import me.catalysmrl.catamines.command.utils.CommandContext;
import me.catalysmrl.catamines.command.utils.CommandException;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Messages;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

public class StartTasksCommand extends AbstractCommand {

    public StartTasksCommand() {
        super("starttasks", "catamines.starttasks", Predicates.any(), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx) throws CommandException {
        plugin.getMineManager().start();
        Messages.sendPrefixed(sender, "&aMine tasks started.");
    }

    @Override
    public Message getDescription() {
        return Message.STARTTASKS_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.STARTTASKS_USAGE;
    }
}
