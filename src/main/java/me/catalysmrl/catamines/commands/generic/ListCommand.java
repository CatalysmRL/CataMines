package me.catalysmrl.catamines.commands.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.AbstractCommand;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.LegacyMessage;
import me.catalysmrl.catamines.utils.message.Messages;
import org.bukkit.command.CommandSender;

public class ListCommand extends AbstractCommand {
    public ListCommand() {
        super("list", "catamines.list", Predicates.any(), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx) throws CommandException {
        LegacyMessage.LIST_MINES_HEADER.send(sender);
        Messages.sendColorized(sender, "&a" + String.join("&d, &a", plugin.getMineManager().getMineList()));
    }

    @Override
    public String getDescription() {
        return LegacyMessage.LIST_DESCRIPTION.getMessage();
    }

    @Override
    public String getUsage() {
        return "/cm list";
    }
}
