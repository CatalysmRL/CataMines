package me.catalysmrl.catamines.command.utils;

import org.bukkit.command.CommandSender;

import me.catalysmrl.catamines.command.abstraction.Command;

public abstract class CommandException extends Exception {

    protected abstract void handle(CommandSender sender);

    public void handle(CommandSender sender, Command command) {
        handle(sender);
    }
}
