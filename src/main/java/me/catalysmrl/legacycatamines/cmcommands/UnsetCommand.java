package me.catalysmrl.legacycatamines.cmcommands;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.legacycatamines.commandhandler.CommandInterface;
import me.catalysmrl.legacycatamines.schedulers.MineManager;
import me.catalysmrl.legacycatamines.mine.mines.CuboidCataMine;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class UnsetCommand implements CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (!sender.hasPermission("catamines.unset")) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.No-Permission"));
            return true;
        }

        if (!(args.length == 2 || args.length == 3)) {
            sender.sendMessage(CataMines.PREFIX + "/cm unset <mine> (block)");
            return true;
        }

        if (!MineManager.getInstance().getMineListNames().contains(args[1])) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Mine.Not-Exist"));
            return true;
        }

        CuboidCataMine cuboidCataMine = MineManager.getInstance().getMine(args[1]);

        if (args.length == 2) {
            cuboidCataMine.clearComposition();
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Unset.Clear")
                    .replaceAll("%mine%", args[1]));
        } else {

            BlockData blockData = Bukkit.createBlockData(args[2].toLowerCase());

            if (cuboidCataMine.containsBlockData(blockData)) {
                cuboidCataMine.removeBlock(blockData);
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Unset.Remove.Success")
                        .replaceAll("%material%", blockData.getAsString())
                        .replaceAll("%mine%", args[1])
                        .replaceAll("%remainingChance%", String.valueOf(100 - cuboidCataMine.getCompositionChance())));
            } else {
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Unset.Remove.Error")
                        .replaceAll("%material%", blockData.getAsString())
                        .replaceAll("%mine%", args[1])
                        .replaceAll("%remainingChance%", String.valueOf(100 - cuboidCataMine.getCompositionChance())));
                return true;
            }
        }

        cuboidCataMine.save();

        return true;
    }

}
