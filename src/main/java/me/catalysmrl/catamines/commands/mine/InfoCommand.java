package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.command.utils.MineTarget;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

public class InfoCommand extends AbstractMineCommand {
    public InfoCommand() {
        super("info", "catamines.info", Predicates.any(), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target)
            throws CommandException {
        assertArgLength(ctx);
    }

    @Override
    public Message getDescription() {
        return Message.INFO_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.INFO_USAGE;
    }
}
