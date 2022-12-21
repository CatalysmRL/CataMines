package me.catalysmrl.legacycatamines.commandhandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public interface CommandInterface {

    boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args);

    default List<String> aliases() {
        return Collections.emptyList();
    }

}
