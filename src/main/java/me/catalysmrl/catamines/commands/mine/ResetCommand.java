package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.AbstractCataMineCommand;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ResetCommand extends AbstractCataMineCommand {
    public ResetCommand() {
        super("reset", null, Predicates.inRange(1, 2), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) {

        if (!sender.hasPermission("catamines.command.reset") || !sender.hasPermission("catamines.command.reset." + mine.getName())) {
            Message.NO_PERMISSION.send(sender);
            return;
        }

        // TODO: Silent resetting and other flags
        mine.reset();
        Message.RESET_SUCCESS.send(sender, mine.getName());
    }

    @Override
    public String getDescription() {
        return Message.RESET_DESCRIPTION.getMessage();
    }

    @Override
    public String getUsage() {
        return "/cm reset <mine> (-s)";
    }
}
