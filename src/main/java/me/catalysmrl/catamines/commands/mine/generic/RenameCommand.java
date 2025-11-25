package me.catalysmrl.catamines.commands.mine.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RenameCommand extends AbstractMineCommand {
    public RenameCommand() {
        super("rename", "catamines.rename", Predicates.equals(1), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine) throws CommandException {
        assertArgLength(ctx);

        String oldMineName = mine.getName();
        mine.setName(ctx.peek());
        try {
            plugin.getMineManager().saveMine(mine);
        } catch (IOException e) {
            Message.MINE_SAVE_EXCEPTION.send(sender, mine.getName());
            mine.setName(oldMineName);
            return;
        }

        Path mineFileToDelete = plugin.getMineManager().getMinesPath().resolve(oldMineName + ".yml");
        try {
            Files.delete(mineFileToDelete);
        } catch (IOException e) {
            Message.MINE_DELETE_EXCEPTION.send(sender, mine.getName());
            return;
        }

        Message.RENAME_SUCCESS.send(sender, oldMineName, mine.getName());
    }

    @Override
    public String getDescription() {
        return Message.RENAME_DESCRIPTION.getKey();
    }

    @Override
    public String getUsage() {
        return null;
    }
}
