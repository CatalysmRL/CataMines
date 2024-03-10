package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.AbstractCataMineCommand;
import me.catalysmrl.catamines.utils.helper.Predicates;
import org.bukkit.command.CommandSender;

import java.util.List;

public class DelayCommand extends AbstractCataMineCommand {

    public DelayCommand() {
        super("setdelay", "catamines.command.setdelay", Predicates.inRange(1, 2), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) {

    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getUsage() {
        return "/cm setdelay <mine> <value> [format]";
    }
}
