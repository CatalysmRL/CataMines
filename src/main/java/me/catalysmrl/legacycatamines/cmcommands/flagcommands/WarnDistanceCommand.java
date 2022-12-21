package me.catalysmrl.legacycatamines.cmcommands.flagcommands;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.legacycatamines.commandhandler.CommandInterface;
import me.catalysmrl.legacycatamines.schedulers.MineManager;
import me.catalysmrl.legacycatamines.mine.mines.CuboidCataMine;
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
