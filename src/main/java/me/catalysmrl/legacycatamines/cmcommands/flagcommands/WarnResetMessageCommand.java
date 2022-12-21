package me.catalysmrl.legacycatamines.cmcommands.flagcommands;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.legacycatamines.commandhandler.CommandInterface;
import me.catalysmrl.legacycatamines.schedulers.MineManager;
import me.catalysmrl.legacycatamines.mine.mines.CuboidCataMine;
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
