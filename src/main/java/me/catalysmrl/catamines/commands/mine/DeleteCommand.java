package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.command.utils.MineTarget;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class DeleteCommand extends AbstractMineCommand {
    public DeleteCommand() {
        super("delete", "catamines.delete", Predicates.equals(0), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target)
            throws CommandException {
        assertArgLength(ctx);

        CataMine mine = target.getMine();

        try {
            plugin.getMineManager().deleteMine(mine);
        } catch (IOException e) {
            Message.DELETE_EXCEPTION.send(sender);
            return;
        }

        Message.DELETE_SUCCESS.send(sender, mine.getName());
    }

    @Override
    public Message getDescription() {
        return Message.DELETE_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.DELETE_USAGE;
    }
}
