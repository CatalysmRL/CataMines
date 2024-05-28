package me.catalysmrl.catamines.commands.mine.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.utils.helper.Predicates;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TimerCommand extends AbstractMineCommand {

    public TimerCommand() {
        super("timer", "catamines.timer", Predicates.inRange(1, 2), false);
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
        return "/cm delay <mine> <value> [format]";
    }
}
