package me.catalysmrl.catamines.command.utils;

import me.catalysmrl.catamines.command.abstraction.Command;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.utils.message.Messages;
import org.bukkit.command.CommandSender;

public abstract class ArgumentException extends CommandException {

    public static class Usage extends ArgumentException {
        @Override
        protected void handle(CommandSender sender) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void handle(CommandSender sender, Command command) {
            Messages.sendPrefixed(sender, command.getUsage());
        }
    }

}
