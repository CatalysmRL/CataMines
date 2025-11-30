package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.AbstractCommand;
import me.catalysmrl.catamines.command.utils.CommandContext;
import me.catalysmrl.catamines.command.utils.CommandException;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Messages;
import me.catalysmrl.catamines.utils.message.Message;
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
    public Message getDescription() {
        return Message.SYNC_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.SYNC_USAGE;
    }
}
