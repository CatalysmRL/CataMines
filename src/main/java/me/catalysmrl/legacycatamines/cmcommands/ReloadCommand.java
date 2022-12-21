package me.catalysmrl.legacycatamines.cmcommands;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.legacycatamines.commandhandler.CommandInterface;
import me.catalysmrl.legacycatamines.schedulers.MineManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (!sender.hasPermission("catamines.reload")) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.No-Permission"));
            return true;
        }

        if (args.length == 1) {
            CataMines.getPlayerMenuUtilityMap().clear();
            CataMines.getInstance().getFileManager().setupFiles();
            MineManager.getInstance().reloadMines();
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Reload.All"));
            return true;
        }

        if (args.length == 2) {

            if (args[1].equalsIgnoreCase("mines")) {
                MineManager.getInstance().reloadMines();
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Reload.Mines"));
                return true;
            }

            if (args[1].equalsIgnoreCase("properties")) {
                CataMines.getInstance().getFileManager().setupCustomFiles();
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Reload.Properties"));
                return true;
            }

            if (args[1].equalsIgnoreCase("config")) {
                CataMines.getInstance().getFileManager().setupConfig();
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Reload.Config"));
                return true;
            }

            if (args[1].equalsIgnoreCase("messages")) {
                CataMines.getInstance().reloadLanguages();
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Reload.Messages"));
                return true;
            }

            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Unknown-Command"));

        } else sender.sendMessage(CataMines.PREFIX + "/cm reload (arg)");

        return true;
    }
}
