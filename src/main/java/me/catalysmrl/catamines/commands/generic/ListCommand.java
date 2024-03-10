package me.catalysmrl.catamines.commands.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.AbstractCataCommand;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.message.Messages;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListCommand extends AbstractCataCommand {
    public ListCommand() {
        super("list", "catamines.command.list", integer -> true, false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args) throws CommandException {
        Message.LIST_MINES_HEADER.send(sender);
        Messages.sendColorized(sender, "&a" + String.join("&d, &a", plugin.getMineManager().getMineList()));
    }

    @Override
    public String getDescription() {
        return Message.LIST_DESCRIPTION.getMessage();
    }

    @Override
    public String getUsage() {
        return "/cm list";
    }
}
