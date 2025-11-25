package me.catalysmrl.catamines.commands.mine.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.AbstractCommand;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Messages;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.StringJoiner;

public class ListCommand extends AbstractCommand {

    public ListCommand() {
        super("list", "catamines.list", Predicates.any(), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx) throws CommandException {
        Messages.sendPrefixed(sender, "List of mines:");
        
        Collection<String> mines = plugin.getMineManager().getMineList();
        if (mines.isEmpty()) {
            Messages.sendPrefixed(sender, "No mines found!");
            return;
        }

        StringJoiner joiner = new StringJoiner(", ");
        for (String mine : mines) {
            joiner.add(mine);
        }
        
        Messages.sendPrefixed(sender, joiner.toString());
    }

    @Override
    public String getDescription() {
        return "Lists all mines";
    }

    @Override
    public String getUsage() {
        return "/cm list";
    }
}
