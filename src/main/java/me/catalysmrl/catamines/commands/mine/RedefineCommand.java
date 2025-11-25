package me.catalysmrl.catamines.commands.mine;

import com.sk89q.worldedit.regions.RegionSelector;
import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.worldedit.WorldEditUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class RedefineCommand extends AbstractMineCommand {
    public RedefineCommand() {
        super("redefine", "catamines.redefine", Predicates.inRange(0, 1), true);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine)
            throws CommandException {
        assertArgLength(ctx);

        String regionName = ctx.hasNext() ? ctx.peek() : "default";
        Optional<CataMineRegion> regionOptional = mine.getRegionManager().get(regionName);
        if (regionOptional.isEmpty()) {
            Message.REGIONS_NOT_EXISTS.send(sender);
            return;
        }

        Player player = (Player) sender;
        CataMineRegion region = regionOptional.get();
        RegionSelector regionSelector = WorldEditUtils.getSelector(player);

        if (!regionSelector.isDefined()) {
            Message.SET_INVALID_REGION.send(player);
            return;
        }

        region.redefineRegion(regionSelector);
        Message.REDEFINE_SUCCESS.send(player, regionName);

        requireSave();
    }

    @Override
    public Message getDescription() {
        return Message.REDEFINE_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.REDEFINE_USAGE;
    }
}
