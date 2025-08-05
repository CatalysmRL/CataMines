package me.catalysmrl.catamines.commands.mine.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.LegacyMessage;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class DeleteCommand extends AbstractMineCommand {
    public DeleteCommand() {
        super("delete", "catamines.delete", Predicates.equals(0), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine) throws CommandException {
        assertArgLength(ctx);

        try {
            plugin.getMineManager().deleteMine(mine);
        } catch (IOException e) {
            LegacyMessage.DELETE_EXCEPTION.send(sender);
            return;
        }

        LegacyMessage.DELETE_SUCCESS.send(sender, mine.getName());
    }

    @Override
    public String getDescription() {
        return LegacyMessage.DELETE_DESCRIPTION.getMessage();
    }

    @Override
    public String getUsage() {
        return "/cm delete <mine>";
    }
}
