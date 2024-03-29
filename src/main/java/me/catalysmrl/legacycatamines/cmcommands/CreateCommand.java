package me.catalysmrl.legacycatamines.cmcommands;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Region;
import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.legacycatamines.commandhandler.CommandInterface;
import me.catalysmrl.legacycatamines.schedulers.MineManager;
import me.catalysmrl.legacycatamines.utils.Utils;
import me.catalysmrl.legacycatamines.mine.mines.CuboidCataMine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateCommand implements CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(CataMines.PREFIX + "Only players may execute this command!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("catamines.create")) {
            player.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.No-Permission"));
            return true;
        }

        if (args.length == 2) {

            if (MineManager.getInstance().getMineListNames().contains(args[1])) {
                player.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Mine.Exist"));
                return true;
            }

            WorldEditPlugin worldEditPlugin = CataMines.getInstance().getWorldEditPlugin();
            Region selection = Utils.getWorldEditSelectionOfPlayer(worldEditPlugin, player);
            if (selection != null) {

                CuboidCataMine cuboidCataMine = new CuboidCataMine(args[1], selection.clone());
                MineManager.getInstance().getMines().add(cuboidCataMine);
                cuboidCataMine.save();
                player.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Create").replaceAll("%mine%", args[1]));
            }
        } else player.sendMessage(CataMines.PREFIX + "/cm create <mine>");
        return true;
    }
}
