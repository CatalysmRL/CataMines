package me.catalysmrl.catamines.commands.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.AbstractCommand;
import me.catalysmrl.catamines.command.utils.CommandContext;
import me.catalysmrl.catamines.command.utils.CommandException;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.message.Messages;
import org.bukkit.command.CommandSender;

public class ListCommand extends AbstractCommand {
    public ListCommand() {
        super("list", "catamines.list", Predicates.any(), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx) throws CommandException {
        Message.LIST_MINES_HEADER.send(sender);
        Messages.sendColorized(sender, "&a" + String.join("&d, &a", plugin.getMineManager().getMineList()));
    }

    @Override
    public Message getDescription() {
        return Message.LIST_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.LIST_USAGE;
    }
}