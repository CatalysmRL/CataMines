package de.c4t4lysm.catamines.commands.cmcommands.flagcommands;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.commands.CommandInterface;
import de.c4t4lysm.catamines.schedulers.MineManager;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class WarnGlobalCommand implements CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (args.length == 4) {
            CuboidCataMine cuboidCataMine = MineManager.getInstance().getMine(args[1]);

            if (!(args[3].equalsIgnoreCase("true") || args[3].equalsIgnoreCase("false"))) {
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Mine.Invalid-Boolean"));
                return true;
            }

            cuboidCataMine.setWarnGlobal(Boolean.parseBoolean(args[3]));
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Flag.Warn.Warn-Global").replaceAll("%mine%", args[1]).replaceAll("%arg%", args[3]));
            cuboidCataMine.save();

        } else sender.sendMessage(CataMines.PREFIX + "§b/cm flag <mine> warnglobal §ctrue/false");

        return true;
    }

}
