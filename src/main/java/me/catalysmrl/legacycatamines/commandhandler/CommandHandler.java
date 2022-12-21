package me.catalysmrl.legacycatamines.commandhandler;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.legacycatamines.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Set;

public class CommandHandler implements CommandExecutor {

    private static final HashMap<String, CommandInterface> commands = new HashMap<>();

    public static Set<String> getCommandNames() {
        return commands.keySet();
    }

    public void register(String name, CommandInterface cmd) {
        commands.put(name, cmd);
    }

    public boolean exists(String name) {
        return commands.containsKey(name);
    }

    public CommandInterface getExecutor(String name) {
        return commands.get(name);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            getExecutor("help").onCommand(sender, command, label, args);
            return true;
        }

        if (exists(args[0].toLowerCase())) {
            getExecutor(args[0].toLowerCase()).onCommand(sender, command, label, args);
            Utils.updateMenus();
        } else {
            if (!sender.hasPermission("catamines.*")) {
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.No-Permission"));
                return true;
            }
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Unknown-Command"));
        }
        return true;
    }
}
