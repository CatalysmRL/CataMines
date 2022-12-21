package me.catalysmrl.legacycatamines.cmcommands;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.legacycatamines.commandhandler.CommandInterface;
import me.catalysmrl.legacycatamines.schedulers.MineManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class StartTasksCommand implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (!sender.hasPermission("catamines.stoptasks")) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.No-Permission"));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(CataMines.PREFIX + "&b/cm starttasks");
            return true;
        }

        MineManager.getInstance().setStopTasks(false);
        sender.sendMessage(CataMines.getInstance().getLangString("Commands.Start-Tasks"));

        return true;
    }
}
