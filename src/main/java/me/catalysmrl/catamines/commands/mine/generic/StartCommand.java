package me.catalysmrl.catamines.commands.mine.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Messages;
import org.bukkit.command.CommandSender;

public class StartCommand extends AbstractMineCommand {

    public StartCommand() {
        super("start", "catamines.start", Predicates.any(), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine) throws CommandException {
        mine.setStopped(false);
        Messages.sendPrefixed(sender, "&aMine started: " + mine.getName());
        requireSave();
    }

    @Override
    public String getDescription() {
        return "Starts a mine";
    }

    @Override
    public String getUsage() {
        return "/cm start <mine>";
    }
}
