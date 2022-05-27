package de.c4t4lysm.catamines.commands.cmcommands.flagcommands;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.commands.CommandInterface;
import de.c4t4lysm.catamines.schedulers.MineManager;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class WarnHotbarCommand implements CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (args.length != 4) {
            sender.sendMessage(CataMines.PREFIX + "§b/cm flag <mine> warnhotbar true/false");
            return true;
        }

        if (!(args[3].equalsIgnoreCase("true") || args[3].equalsIgnoreCase("false"))) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Mine.Invalid-Boolean"));
            return true;
        }

        CuboidCataMine cuboidCataMine = MineManager.getInstance().getMine(args[1]);

        cuboidCataMine.setWarnHotbar(Boolean.parseBoolean(args[3]));
        sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Flag.Warn.Warn-Hotbar").replaceAll("%mine%", args[1]).replaceAll("%arg%", args[3]));
        cuboidCataMine.save();

        return true;
    }
}
