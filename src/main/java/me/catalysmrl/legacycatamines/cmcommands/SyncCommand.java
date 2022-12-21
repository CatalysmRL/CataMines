package me.catalysmrl.legacycatamines.cmcommands;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.legacycatamines.commandhandler.CommandInterface;
import me.catalysmrl.legacycatamines.schedulers.MineManager;
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
