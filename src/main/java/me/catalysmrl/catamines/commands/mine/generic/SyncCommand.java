package me.catalysmrl.catamines.commands.mine.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.AbstractCommand;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Messages;
import org.bukkit.command.CommandSender;

public class SyncCommand extends AbstractCommand {

    public SyncCommand() {
        super("sync", "catamines.sync", Predicates.any(), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx) throws CommandException {
        plugin.getMineManager().loadMinesFromFolder(plugin.getMineManager().getMinesPath());
        Messages.sendPrefixed(sender, "&aMines synced!");
    }

    @Override
    public String getDescription() {
        return "Syncs mines from disk";
    }

    @Override
    public String getUsage() {
        return "/cm sync";
    }
}
