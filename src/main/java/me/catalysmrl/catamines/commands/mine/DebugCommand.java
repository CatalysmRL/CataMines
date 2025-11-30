package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.command.utils.MineTarget;
import me.catalysmrl.catamines.utils.helper.Predicates;
import org.bukkit.command.CommandSender;
import me.catalysmrl.catamines.utils.message.Message;

public class DebugCommand extends AbstractMineCommand {
    public DebugCommand() {
        super("debug", "catamines.debug", Predicates.any(), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target)
            throws CommandException {
        sender.sendMessage(target.getMine().toString());
    }

    @Override
    public Message getDescription() {
        return Message.DEBUG_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.DEBUG_USAGE;
    }
}
