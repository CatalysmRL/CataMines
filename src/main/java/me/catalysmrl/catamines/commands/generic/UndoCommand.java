package me.catalysmrl.catamines.commands.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.AbstractCommand;
import me.catalysmrl.catamines.command.utils.CommandContext;
import me.catalysmrl.catamines.command.utils.CommandException;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.undo.UndoManager;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class UndoCommand extends AbstractCommand {

    public UndoCommand() {
        super("undo", "catamines.command.undo", Predicates.any(), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx) throws CommandException {
        UndoManager.Result result = UndoManager.undoLast(sender, plugin);
        if (result == null) {
            Message.UNDO_EMPTY.send(sender);
            return;
        }

        Message.UNDO_SUCCESS.send(sender, result.restored());
        Message.UNDO_FOOTER.send(sender, result.description());
    }

    @Override
    public Message getDescription() {
        return Message.UNDO_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.UNDO_USAGE;
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx) {
        return Collections.emptyList();
    }
}
