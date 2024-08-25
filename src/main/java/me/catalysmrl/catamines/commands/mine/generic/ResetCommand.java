package me.catalysmrl.catamines.commands.mine.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ResetCommand extends AbstractMineCommand {
    public ResetCommand() {
        super("reset", null, Predicates.inRange(1, 2), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) {

        if (!sender.hasPermission("catamines.reset") || !sender.hasPermission("catamines.reset." + mine.getName())) {
            Message.NO_PERMISSION.send(sender);
            return;
        }

        // TODO: Silent resetting and other flags
        mine.reset(plugin);
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
