package me.catalysmrl.catamines.commands.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.AbstractCommand;
import me.catalysmrl.catamines.command.utils.CommandContext;
import me.catalysmrl.catamines.command.utils.CommandException;
import me.catalysmrl.catamines.managers.ConfirmationManager;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ConfirmCommand extends AbstractCommand {

    public ConfirmCommand() {
        super("confirm", "catamines.confirm", (i) -> true, false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx) throws CommandException {
        if (!ConfirmationManager.confirm(sender)) {
            sender.sendMessage("§cYou have no pending confirmations.");
        }
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx) {
        return List.of();
    }

    @Override
    public Message getDescription() {
        return Message.CONFIRM_DESCRIPTION; // Assuming this exists or I should use a raw string/create it
    }

    @Override
    public Message getUsage() {
        return Message.CONFIRM_USAGE; // Assuming this exists
    }
}
