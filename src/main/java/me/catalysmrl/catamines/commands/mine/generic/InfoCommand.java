package me.catalysmrl.catamines.commands.mine.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class InfoCommand extends AbstractMineCommand {
    public InfoCommand() {
        super("info", "catamines.info", integer -> integer == 1, false);
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
        return null;
    }
}
