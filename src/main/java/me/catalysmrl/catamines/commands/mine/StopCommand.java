package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Messages;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

public class StopCommand extends AbstractMineCommand {

    public StopCommand() {
        super("stop", "catamines.stop", Predicates.any(), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine)
            throws CommandException {
        mine.getFlags().setStopped(true);
        Messages.sendPrefixed(sender, "&aMine stopped: " + mine.getName());
        requireSave();
    }

    @Override
    public Message getDescription() {
        return Message.STOP_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.STOP_USAGE;
    }
}
