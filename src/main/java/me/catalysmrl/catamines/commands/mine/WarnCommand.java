package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.command.utils.CommandContext;
import me.catalysmrl.catamines.command.utils.CommandException;
import me.catalysmrl.catamines.command.utils.MineTarget;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

public class WarnCommand extends AbstractMineCommand {

    public WarnCommand() {
        super("warn", "catamines.warn", Predicates.inRange(2, 3), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target)
            throws CommandException {

    }

    @Override
    public Message getDescription() {
        return Message.WARN_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.WARN_USAGE;
    }
}
