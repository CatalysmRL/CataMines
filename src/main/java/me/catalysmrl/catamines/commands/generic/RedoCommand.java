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

public class RedoCommand extends AbstractCommand {

    public RedoCommand() {
        super("redo", "catamines.command.redo", Predicates.any(), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx) throws CommandException {
        UndoManager.Result result = UndoManager.redoLast(sender, plugin);
        if (result == null) {
            Message.REDO_EMPTY.send(sender);
            return;
        }

        Message.REDO_SUCCESS.send(sender, result.restored());
        Message.REDO_FOOTER.send(sender, result.description());
    }

    @Override
    public Message getDescription() {
        return Message.REDO_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.REDO_USAGE;
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx) {
        return Collections.emptyList();
    }
}
