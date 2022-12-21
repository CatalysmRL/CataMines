package me.catalysmrl.legacycatamines.cmcommands;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.legacycatamines.commandhandler.CommandInterface;
import me.catalysmrl.legacycatamines.schedulers.MineManager;
import me.catalysmrl.legacycatamines.mine.mines.CuboidCataMine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ResetCommand implements CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 2) {

            if (!sender.hasPermission("catamines.reset") && !sender.hasPermission("catamines.reset." + args[1])) {
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.No-Permission"));
                return true;
            }

            if (!MineManager.getInstance().getMineListNames().contains(args[1])) {
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Mine.Not-Exist"));
                return true;
            }

            CuboidCataMine cuboidCataMine = MineManager.getInstance().getMine(args[1]);

            if (cuboidCataMine.getRegion() == null || cuboidCataMine.getRandomPattern() == null) {
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Mine.Cant-Reset").replaceAll("%mine%", args[1]));
                return true;
            }

            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Reset"));
            cuboidCataMine.forceReset();
        } else sender.sendMessage(CataMines.PREFIX + "§b/cm reset <mine>");

        return true;
    }
}
