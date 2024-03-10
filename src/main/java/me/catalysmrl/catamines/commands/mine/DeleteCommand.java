package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.AbstractCataMineCommand;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.List;

public class DeleteCommand extends AbstractCataMineCommand {
    public DeleteCommand() {
        super("delete", "catamines.command.delete", i -> i == 1, false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) {

        try {
            plugin.getMineManager().deleteMine(mine);
        } catch (IOException e) {
            Message.DELETE_EXCEPTION.send(sender);
            return;
        }

        Message.DELETE_SUCCESS.send(sender, mine.getName());
    }

    @Override
    public String getDescription() {
        return Message.DELETE_DESCRIPTION.getMessage();
    }

    @Override
    public String getUsage() {
        return "/cm delete <mine>";
    }
}
