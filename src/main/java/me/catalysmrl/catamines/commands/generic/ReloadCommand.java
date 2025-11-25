package me.catalysmrl.catamines.commands.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.AbstractCommand;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;

import org.bukkit.command.CommandSender;

public class ReloadCommand extends AbstractCommand {
    public ReloadCommand() {
        super("reload", "catamines.reload", Predicates.inRange(0, 1), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx) throws CommandException {
        assertArgLength(ctx);
        plugin.getMineManager().loadMinesFromFolder(plugin.getMineManager().getMinesPath());
    }

    @Override
    public Message getDescription() {
        return Message.RELOAD_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.RELOAD_USAGE;
    }
}
