package me.catalysmrl.legacycatamines.cmcommands;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.legacycatamines.commandhandler.CommandInterface;
import me.catalysmrl.legacycatamines.schedulers.MineManager;
import me.catalysmrl.legacycatamines.mine.mines.CuboidCataMine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class StopCommand implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (!sender.hasPermission("catamines.stop")) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.No-Permission"));
            return true;
        }

        if (args.length == 2) {
            if (!MineManager.getInstance().getMineListNames().contains(args[1])) {
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Mine.Not-Exist"));
                return true;
            }

            CuboidCataMine cuboidCataMine = MineManager.getInstance().getMine(args[1]);
            cuboidCataMine.setStopped(true);
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Stop").replaceAll("%mine%", args[1]));
            cuboidCataMine.save();
        } else sender.sendMessage(CataMines.PREFIX + "/cm stop <mine>");

        return true;
    }
}
