package me.catalysmrl.catamines.commands.mine.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.AbstractCommand;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Messages;
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
    public String getDescription() {
        return "Starts the mine manager tasks";
    }

    @Override
    public String getUsage() {
        return "/cm starttasks";
    }
}
