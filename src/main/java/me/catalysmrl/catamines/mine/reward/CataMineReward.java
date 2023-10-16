package me.catalysmrl.catamines.mine.reward;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.List;

public abstract class CataMineReward implements ConfigurationSerializable {

    List<String> commandsToExecute = new ArrayList<>();

    public void executeCommands(CommandSender sender) {
        commandsToExecute.forEach(s -> Bukkit.dispatchCommand(sender, s));
    }
}
