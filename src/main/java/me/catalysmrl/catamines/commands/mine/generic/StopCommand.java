package me.catalysmrl.catamines.commands.mine.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Messages;
import org.bukkit.command.CommandSender;

public class StopCommand extends AbstractMineCommand {

    public StopCommand() {
        super("stop", "catamines.stop", Predicates.any(), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine) throws CommandException {
        mine.setStopped(true);
        Messages.sendPrefixed(sender, "&aMine stopped: " + mine.getName());
        requireSave();
    }

    @Override
    public String getDescription() {
        return "Stops a mine";
    }

    @Override
    public String getUsage() {
        return "/cm stop <mine>";
    }
}
