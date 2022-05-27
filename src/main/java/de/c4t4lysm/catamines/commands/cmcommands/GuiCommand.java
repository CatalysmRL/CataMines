package de.c4t4lysm.catamines.commands.cmcommands;

import de.c4t4lysm.catamines.CataMines;
import de.c4t4lysm.catamines.commands.CommandInterface;
import de.c4t4lysm.catamines.schedulers.MineManager;
import de.c4t4lysm.catamines.utils.menusystem.menus.MineListMenu;
import de.c4t4lysm.catamines.utils.menusystem.menus.MineMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuiCommand implements CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Only-Players"));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("catamines.gui")) {
            player.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.No-Permission"));
            return true;
        }


        if (args.length == 1) {

            if (CataMines.getPlayerMenuUtility(player).getMenu() == null) {
                new MineListMenu(CataMines.getPlayerMenuUtility(player)).open();
            } else {
                CataMines.getPlayerMenuUtility(player).getMenu().open();
            }

        } else if (args.length == 2) {

            if (!MineManager.getInstance().getMineListNames().contains(args[1])) {
                player.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Mine.Not-Exist"));
                return true;
            }

            new MineMenu(CataMines.getPlayerMenuUtility(player), MineManager.getInstance().getMine(args[1])).open();

        } else {
            player.sendMessage(CataMines.PREFIX + "§b/cm gui (mine)");
        }


        return true;
    }
}
