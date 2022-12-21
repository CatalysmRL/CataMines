package me.catalysmrl.catamines.commands.mine;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.AbstractCataCommand;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.mine.abstraction.CataMine;
import me.catalysmrl.catamines.mine.abstraction.region.AbstractCataMineRegion;
import me.catalysmrl.catamines.mine.mines.RegionCataMine;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.worldedit.WorldEditUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CreateCommand extends AbstractCataCommand {

    public CreateCommand() {
        super("create", "catamines.command.create", integer -> integer == 1, true);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) throws CommandException {
        if (args.get(0).equals("*")) {
            Message.MINE_INVALID_NAME.send(sender);
            return;
        }

        if (plugin.getMineManager().containsMine(args.get(0))) {
            Message.MINE_EXIST.send(sender, args.get(0));
            return;
        }

        Player player = (Player) sender;

        RegionSelector regionSelector = WorldEditUtils.getSelector(player);
        Region region;

        try {
            region = regionSelector.getRegion();
        } catch (IncompleteRegionException e) {
            Message.INCOMPLETE_REGION.send(player);
            return;
        }

        CataMine cataMine = new RegionCataMine(args.get(0), );
        plugin.getMineManager().registerMine(cataMine);

        Message.CREATE.send(player, args.get(0));
    }

    @Override
    public String getDescription() {
        return Message.CREATE_DESCRIPTION.getMessage();
    }

    @Override
    public String getUsage() {
        return "/cm create <mine>";
    }
}
