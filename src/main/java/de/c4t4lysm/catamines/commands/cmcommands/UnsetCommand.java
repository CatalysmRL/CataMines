package de.c4t4lysm.catamines.commands.cmcommands;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.commands.CommandInterface;
import de.c4t4lysm.catamines.schedulers.MineManager;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.Material;
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

            if (Material.getMaterial(args[2].toUpperCase()) == null) {
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Mine.Material-Not-Found"));
                return true;
            }

            Material material = Material.getMaterial(args[2].toUpperCase());

            if (cuboidCataMine.containsBlockMaterial(material)) {
                cuboidCataMine.removeBlock(material);
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Unset.Remove.Success")
                        .replaceAll("%material%", material.name())
                        .replaceAll("%mine%", args[1])
                        .replaceAll("%remainingChance%", String.valueOf(100 - cuboidCataMine.getCompositionChance())));
            } else {
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Unset.Remove.Error")
                        .replaceAll("%material%", material.name())
                        .replaceAll("%mine%", args[1])
                        .replaceAll("%remainingChance%", String.valueOf(100 - cuboidCataMine.getCompositionChance())));
                return true;
            }
        }

        cuboidCataMine.save();

        return true;
    }

}
