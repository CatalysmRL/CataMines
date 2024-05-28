package me.catalysmrl.catamines.commands.mine.regions.subcommands;

import com.sk89q.worldedit.regions.RegionSelector;
import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.mine.components.region.impl.SelectionRegion;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.message.Messages;
import me.catalysmrl.catamines.utils.worldedit.WorldEditUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class RegionCreateCommand extends AbstractMineCommand {
    public RegionCreateCommand() {
        super("create", "catamines.regions.create", i -> i == 2, true);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) {
        String regionName = args.get(0);

        if (mine.getRegionManager().get(regionName).isPresent()) {
            Message.REGION_EXISTS.send(sender);
            return;
        }

        Player player = (Player) sender;
        RegionSelector regionSelector = WorldEditUtils.getSelector(player);
        if (!regionSelector.isDefined()) {
            Message.INCOMPLETE_REGION.send(sender);
            return;
        }

        CataMineRegion region = new SelectionRegion(regionName, regionSelector);
        region.getCompositionManager().add(new CataMineComposition("default"));
        mine.getRegionManager().add(region);
        Message.REGION_CREATE_SUCCESS.send(sender);

        try {
            plugin.getMineManager().saveMine(mine);
        } catch (IOException e) {
            Message.MINE_SAVE_EXCEPTION.send(sender, mine.getName());
        }
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) {
        return args.size() == 1 ? List.of(Messages.colorize("&7<name>")) : Collections.emptyList();
    }

    @Override
    public String getDescription() {
        return Message.REGION_CREATE_DESCRIPTION.getMessage();
    }

    @Override
    public String getUsage() {
        return "/cm regions <mine> create <name>";
    }
}
