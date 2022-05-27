package de.c4t4lysm.catamines.commands.cmcommands;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.commands.CommandInterface;
import de.c4t4lysm.catamines.schedulers.MineManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class StopTasksCommand implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (!sender.hasPermission("catamines.stoptasks")) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.No-Permission"));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(CataMines.PREFIX + "&b/cm stoptasks");
            return true;
        }

        MineManager.getInstance().setStopTasks(true);
        sender.sendMessage(CataMines.getInstance().getLangString("Commands.Stop-Tasks"));

        return true;
    }
}
