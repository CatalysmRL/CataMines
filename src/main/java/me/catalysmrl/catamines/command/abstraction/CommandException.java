package me.catalysmrl.catamines.command.abstraction;

import org.bukkit.command.CommandSender;

public abstract class CommandException extends Exception {

    protected abstract void handle(CommandSender sender);

    public void handle(CommandSender sender, CataCommand command) {
        handle(sender);
    }
}
