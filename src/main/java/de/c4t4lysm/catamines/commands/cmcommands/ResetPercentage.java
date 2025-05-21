package de.c4t4lysm.catamines.commands.cmcommands;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.commands.CommandInterface;
import de.c4t4lysm.catamines.schedulers.MineManager;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ResetPercentage implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (!sender.hasPermission("catamines.resetpercentage")) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.No-Permission"));
            return true;
        }

        if (args.length != 3) {
            sender.sendMessage(CataMines.PREFIX + "/cm resetpercentage <mine> <percentage>");
            return true;
        }

        if (!MineManager.getInstance().getMineListNames().contains(args[1])) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Mine.Not-Exist"));
            return true;
        }

        String percentArg = args[2];
        if (percentArg.endsWith("%")) percentArg = StringUtils.chop(percentArg);

        double resetPercentInput;
        try {
            resetPercentInput = Double.parseDouble(percentArg);
        } catch (NumberFormatException e) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Not-Number"));
            return true;
        }

        if (resetPercentInput < 0 || resetPercentInput > 100) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Invalid-Range")
                    .replaceAll("%start%", "0.0")
                    .replaceAll("%end%", "100.0"));
            return true;
        }

        CuboidCataMine mine = MineManager.getInstance().getMine(args[1]);
        mine.setResetPercentage(resetPercentInput);

        sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Reset-Percentage")
                .replaceAll("%mine%", mine.getName())
                .replaceAll("%percentage%", String.valueOf(resetPercentInput)));

        mine.save();
        return true;
    }
}
