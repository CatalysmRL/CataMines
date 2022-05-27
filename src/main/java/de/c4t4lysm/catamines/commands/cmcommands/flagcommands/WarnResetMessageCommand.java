package de.c4t4lysm.catamines.commands.cmcommands.flagcommands;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.commands.CommandInterface;
import de.c4t4lysm.catamines.schedulers.MineManager;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class WarnResetMessageCommand implements CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (args.length >= 4) {
            CuboidCataMine cuboidCataMine = MineManager.getInstance().getMine(args[1]);

            StringBuilder sb = new StringBuilder();
            for (int i = 3; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }

            String resetMessage = sb.toString().replace('"', ' ').trim();
            cuboidCataMine.setResetMessage(resetMessage);
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Flag.Warn.Reset-Message").replaceAll("%mine%", args[1]));
            cuboidCataMine.save();
        } else sender.sendMessage(CataMines.PREFIX + "§b/cm flag <mine> resetmessage \"message\"");

        return true;
    }
}
