package de.c4t4lysm.catamines.commands.cmcommands;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.commands.CommandInterface;
import de.c4t4lysm.catamines.schedulers.MineManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListCommand implements CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (!sender.hasPermission("catamines.list")) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.No-Permission"));
            return true;
        }

        if (args.length == 1) {
            List<String> mineList = MineManager.getInstance().getMineListNames();
            String str = mineList.toString().replaceAll(",", "§b,§6");
            sender.sendMessage(CataMines.PREFIX + "These are the registered mines:\n§6" + str.substring(1, str.length() - 1));
        } else sender.sendMessage(CataMines.PREFIX + "/cm list");
        return true;
    }
}
