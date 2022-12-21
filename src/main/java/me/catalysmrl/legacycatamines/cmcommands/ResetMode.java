package me.catalysmrl.legacycatamines.cmcommands;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.legacycatamines.commandhandler.CommandInterface;
import me.catalysmrl.legacycatamines.schedulers.MineManager;
import me.catalysmrl.legacycatamines.mine.components.CataMineResetMode;
import me.catalysmrl.legacycatamines.mine.mines.CuboidCataMine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ResetMode implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (!sender.hasPermission("catamines.resetmode")) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.No-Permission"));
            return true;
        }

        if (args.length != 3) {
            sender.sendMessage(CataMines.PREFIX + "/cm resetmode <mine> <mode>");
            return true;
        }

        if (!MineManager.getInstance().getMineListNames().contains(args[1])) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Mine.Not-Exist"));
            return true;
        }

        if (!(args[2].equalsIgnoreCase("time") || args[2].equalsIgnoreCase("percentage"))) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Mine.Invalid-Reset-Mode"));
            return true;
        }

        CuboidCataMine mine = MineManager.getInstance().getMine(args[1]);
        mine.setResetMode(CataMineResetMode.valueOf(args[2].toUpperCase()));

        sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Reset-Mode")
                .replaceAll("%mine%", mine.getName())
                .replaceAll("%mode%", args[2].toUpperCase()));

        mine.save();

        return true;
    }
}
