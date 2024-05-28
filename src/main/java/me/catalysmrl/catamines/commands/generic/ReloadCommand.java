package me.catalysmrl.catamines.commands.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.AbstractCommand;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.utils.helper.Predicates;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends AbstractCommand {
    public ReloadCommand() {
        super("reload", "catamines.reload", Predicates.inRange(0, 1), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args) throws CommandException {

    }

    @Override
    public String getDescription() {
        return "Reloads the plugin";
    }

    @Override
    public String getUsage() {
        return "/cm reload [arg]";
    }
}
