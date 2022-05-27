package de.c4t4lysm.catamines.commands.cmcommands.flagcommands;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.commands.CommandInterface;
import de.c4t4lysm.catamines.schedulers.MineManager;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class WarnDistanceCommand implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (args.length == 4) {
            CuboidCataMine cuboidCataMine = MineManager.getInstance().getMine(args[1]);
            int warnDistance;
            try {
                warnDistance = Integer.parseInt(args[3]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(CataMines.PREFIX + "Your distance must be an §cInteger§7.");
                return true;
            }

            cuboidCataMine.setWarnDistance(warnDistance);
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Flag.Warn.Warn-Distance").replaceAll("%mine%", args[1]).replaceAll("%arg%", args[3]));
            cuboidCataMine.save();
        } else sender.sendMessage(CataMines.PREFIX + "§b/cm flag <mine> warndistance [distance]");

        return true;
    }
}
