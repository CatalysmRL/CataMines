package de.c4t4lysm.catamines.commands.cmcommands;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.commands.CommandInterface;
import de.c4t4lysm.catamines.schedulers.MineManager;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SetDelayCommand implements CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (!sender.hasPermission("catamines.setdelay")) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.No-Permission"));
            return true;
        }

        if (args.length == 3) {

            if (!MineManager.getInstance().getMineListNames().contains(args[1])) {
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Mine.Not-Exist"));
                return true;
            }

            CuboidCataMine cuboidCataMine = MineManager.getInstance().getMine(args[1]);

            int resetDelay;

            try {
                resetDelay = Integer.parseInt(args[2]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Mine.Not-Number"));
                return true;
            }

            if (resetDelay > 1000000) {
                sender.sendMessage(CataMines.PREFIX + "Reset delay must be be lower than §c1000000 §7seconds.");
                return true;
            }

            cuboidCataMine.setResetDelay(resetDelay);
            cuboidCataMine.setCountdown(resetDelay);
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Delay").replaceAll("%mine%", args[1]).replaceAll("%seconds%", args[2]));
            cuboidCataMine.save();

        } else sender.sendMessage(CataMines.PREFIX + "/cm setdelay <mine> [seconds]");

        return true;
    }
}
