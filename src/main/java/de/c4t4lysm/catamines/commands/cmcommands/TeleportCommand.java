package de.c4t4lysm.catamines.commands.cmcommands;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.commands.CommandInterface;
import de.c4t4lysm.catamines.schedulers.MineManager;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {


        if (args.length < 2 || args.length > 3) {
            sender.sendMessage(CataMines.PREFIX + "§b/cm tp <mine> (player)");
            return true;
        }

        if (!sender.hasPermission("catamines.teleport") && !sender.hasPermission("catamines.teleport." + args[1])) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.No-Permission"));
            return true;
        }

        if (!MineManager.getInstance().getMineListNames().contains(args[1])) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Mine.Not-Exist"));
            return true;
        }

        CuboidCataMine cuboidCataMine = MineManager.getInstance().getMine(args[1]);
        if (cuboidCataMine.getTeleportLocation() == null) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Mine.Teleport-Error"));
            return true;
        }

        if (args.length == 2) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Only-Players"));
                return true;
            }
            Player player = (Player) sender;
            player.teleport(cuboidCataMine.getTeleportLocation());
            player.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Teleport.Self").replaceAll("%mine%", args[1]));
            return true;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Player-Not-Found").replaceAll("%player%", args[2]));
            return true;
        }

        target.teleport(cuboidCataMine.getTeleportLocation());
        sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Teleport.Other").replaceAll("%mine%", args[1]).replaceAll("%player%", args[2]));
        return true;
    }
}
