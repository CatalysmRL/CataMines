package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

public class UnsetCommand extends AbstractMineCommand {
    public UnsetCommand() {
        super("unset", "catamines.unset", Predicates.equals(1), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine)
            throws CommandException {
        assertArgLength(ctx);
    }

    @Override
    public Message getDescription() {
        return Message.UNSET_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.UNSET_USAGE;
    }
}
