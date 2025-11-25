package me.catalysmrl.catamines.commands.mine.regions.subcommands;

import com.sk89q.worldedit.regions.RegionSelector;
import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.mine.components.region.impl.SelectionRegion;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.message.Messages;
import me.catalysmrl.catamines.utils.worldedit.WorldEditUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class RegionCreateCommand extends AbstractMineCommand {
    public RegionCreateCommand() {
        super("create", "catamines.regions.create", Predicates.equals(1), true);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine)
            throws CommandException {
        assertArgLength(ctx);

        String regionName = ctx.peek();

        if (mine.getRegionManager().get(regionName).isPresent()) {
            Message.REGIONS_EXISTS.send(sender);
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
        Message.REGIONS_CREATE_SUCCESS.send(sender);

        requireSave();
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine) {
        return ctx.remaining() == 1 ? List.of(Messages.colorize("&7<name>")) : Collections.emptyList();
    }

    @Override
    public Message getDescription() {
        return Message.REGIONS_CREATE_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.REGIONS_CREATE_USAGE;
    }
}
