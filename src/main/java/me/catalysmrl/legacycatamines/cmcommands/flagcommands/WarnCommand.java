package me.catalysmrl.legacycatamines.cmcommands.flagcommands;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.legacycatamines.commandhandler.CommandInterface;
import me.catalysmrl.legacycatamines.schedulers.MineManager;
import me.catalysmrl.legacycatamines.mine.mines.CuboidCataMine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class WarnCommand implements CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (args.length == 4) {

            if (!(args[3].equalsIgnoreCase("true") || args[3].equalsIgnoreCase("false"))) {
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Mine.Invalid-Boolean"));
                return true;
            }

            CuboidCataMine cuboidCataMine = MineManager.getInstance().getMine(args[1]);

            cuboidCataMine.setWarn(Boolean.parseBoolean(args[3]));
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Flag.Warn.Warn-Enable").replaceAll("%mine%", args[1]).replaceAll("%arg%", args[3]));
            cuboidCataMine.save();

        } else sender.sendMessage(CataMines.PREFIX + "/cm warn <mine> true/false");


        return true;
    }
}
