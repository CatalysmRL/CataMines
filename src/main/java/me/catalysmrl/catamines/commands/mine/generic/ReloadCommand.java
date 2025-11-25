package me.catalysmrl.catamines.commands.mine.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.AbstractCommand;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Messages;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends AbstractCommand {

    public ReloadCommand() {
        super("reload", "catamines.reload", Predicates.any(), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx) throws CommandException {
        // plugin.reload();
        Messages.sendPrefixed(sender, "&aPlugin reloaded!");
    }

    @Override
    public String getDescription() {
        return "Reloads the plugin";
    }

    @Override
    public String getUsage() {
        return "/cm reload";
    }
}
