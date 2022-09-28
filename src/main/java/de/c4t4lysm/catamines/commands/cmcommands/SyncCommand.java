package de.c4t4lysm.catamines.commands.cmcommands;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.commands.CommandInterface;
import de.c4t4lysm.catamines.schedulers.MineManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SyncCommand implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!sender.hasPermission("catamines.sync")) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.No-Permission"));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(CataMines.PREFIX + "&b/cm sync");
            return true;
        }

        MineManager.getInstance().getMines().forEach(cuboidCataMine -> cuboidCataMine.setCountdown(0));
        sender.sendMessage(CataMines.getInstance().getLangString("Commands.Sync"));
        return true;
    }
}
