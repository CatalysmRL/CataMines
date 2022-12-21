package me.catalysmrl.catamines.commands.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.AbstractCataCommand;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.mine.abstraction.CataMine;
import me.catalysmrl.catamines.utils.helper.Predicates;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends AbstractCataCommand {
    public ReloadCommand() {
        super("reload", "catamines.command.reload", Predicates.inRange(0, 1), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) throws CommandException {

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
