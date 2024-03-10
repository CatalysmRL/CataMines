package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.AbstractCataMineCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class DebugCommand extends AbstractCataMineCommand {
    public DebugCommand() {
        super("debug", "catamines.command.debug", integer -> integer == 1, false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) {
        sender.sendMessage(mine.toString());
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getUsage() {
        return "/cm debug <mine>";
    }
}
