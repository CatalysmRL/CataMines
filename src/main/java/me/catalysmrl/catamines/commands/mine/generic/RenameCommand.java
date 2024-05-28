package me.catalysmrl.catamines.commands.mine.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class RenameCommand extends AbstractMineCommand {
    public RenameCommand() {
        super("rename", "catamines.rename", integer -> integer == 1, false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) {

        String oldMineName = mine.getName();

        mine.setName(args.get(0));
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
        return Message.RENAME_DESCRIPTION.getMessage();
    }

    @Override
    public String getUsage() {
        return null;
    }
}
