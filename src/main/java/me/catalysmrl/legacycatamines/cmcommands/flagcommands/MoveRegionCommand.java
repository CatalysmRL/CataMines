package me.catalysmrl.legacycatamines.cmcommands.flagcommands;

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

public class MoveRegionCommand implements CommandInterface {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Error-Messages.Only-Players"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 3) {

            WorldEditPlugin worldEditPlugin = CataMines.getInstance().getWorldEditPlugin();
            Region selection = Utils.getWorldEditSelectionOfPlayer(worldEditPlugin, player);
            if (selection != null) {
                CuboidCataMine cuboidCataMine = MineManager.getInstance().getMine(args[1]);
                cuboidCataMine.setRegion(selection.clone());
                sender.sendMessage(CataMines.PREFIX + CataMines.getInstance().getLangString("Commands.Flag.Move-Region").replaceAll("%mine%", args[1]));
                cuboidCataMine.save();
            }
        } else player.sendMessage(CataMines.PREFIX + "/cm flag <mine> moveRegion");

        return true;
    }
}
