package me.catalysmrl.legacycatamines.cmcommands;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.legacycatamines.commandhandler.CommandInterface;
import me.catalysmrl.legacycatamines.schedulers.MineManager;
import me.catalysmrl.catamines.mine.components.composition.CataMineBlock;
import me.catalysmrl.legacycatamines.mine.mines.CuboidCataMine;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SetCommand implements CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (!sender.hasPermission("catamines.set")) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.No-Permission"));
            return true;
        }

        if (!(args.length == 3 || args.length == 4)) {
            sender.sendMessage(CataMines.PREFIX + "/cm set <mine> [block] n%, for example: §8/cm set Stone_Mine stone 50%");
            return true;
        }

        if (!MineManager.getInstance().getMineListNames().contains(args[1])) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Mine.Not-Exist"));
            return true;
        }

        BlockData blockData = Bukkit.createBlockData(args[2].toLowerCase());
        CuboidCataMine cuboidCataMine = MineManager.getInstance().getMine(args[1]);

        if (args.length == 3) {

            try {
                cuboidCataMine.addBlock(new CataMineBlock(blockData, 100 - cuboidCataMine.getCompositionChance()));
            } catch (IllegalArgumentException ex) {
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Set.Error")
                        .replaceAll("%chance-over-100%", ex.getMessage())
                        .replaceAll("%remainingChance%", String.valueOf(100 - cuboidCataMine.getCompositionChance())));
                return true;
            }

            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Set.Success")
                    .replaceAll("%material%", blockData.getAsString(true))
                    .replaceAll("%mine%", args[1])
                    .replaceAll("%remainingChance%", String.valueOf(100 - cuboidCataMine.getCompositionChance())));
            cuboidCataMine.save();
            return true;
        } else {

            if (args[3].endsWith("%")) {
                double percentInput;

                try {
                    percentInput = Double.parseDouble(StringUtils.chop(args[3]));
                } catch (NumberFormatException ex) {
                    sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Not-Number"));
                    return true;
                }

                if (percentInput < 0 || percentInput > 100) {
                    sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Invalid-Chance"));
                    return true;
                }

                try {
                    cuboidCataMine.addBlock(new CataMineBlock(blockData, percentInput));
                } catch (IllegalArgumentException ex) {
                    sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Set.Error")
                            .replaceAll("%chance-over-100%", ex.getMessage())
                            .replaceAll("%remainingChance%", String.valueOf(100 - cuboidCataMine.getCompositionChance())));
                    return true;
                }

                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Set.Success")
                        .replaceAll("%material%", blockData.getAsString(true))
                        .replaceAll("%mine%", args[1])
                        .replaceAll("%remainingChance%", String.valueOf(100 - cuboidCataMine.getCompositionChance())));
                cuboidCataMine.save();
                return true;
            } else
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Not-Percentage"));
        }

        return false;
    }

}
