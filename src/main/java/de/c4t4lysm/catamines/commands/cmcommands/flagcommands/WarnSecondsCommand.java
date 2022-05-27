package de.c4t4lysm.catamines.commands.cmcommands.flagcommands;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.commands.CommandInterface;
import de.c4t4lysm.catamines.schedulers.MineManager;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class WarnSecondsCommand implements CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length >= 4) {
            CuboidCataMine cuboidCataMine = MineManager.getInstance().getMine(args[1]);

            List<Integer> integers = new ArrayList<>();
            for (int i = 3; i < args.length; i++) {
                try {
                    integers.add(Integer.parseInt(args[i]));
                } catch (NumberFormatException ex) {
                    sender.sendMessage(CataMines.PREFIX + "One of those arguments was not an §cInteger§7.");
                    return true;
                }
            }

            cuboidCataMine.setWarnSeconds(integers);
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Flag.Warn.Warn-Seconds").replaceAll("%mine%", args[1]));
            cuboidCataMine.save();
        } else sender.sendMessage(CataMines.PREFIX + "For example: §b/cm flag <mine> warnseconds 1 2 3 10 20 60");

        return true;
    }
}
