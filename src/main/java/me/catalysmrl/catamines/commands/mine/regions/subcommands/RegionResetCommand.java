package me.catalysmrl.catamines.commands.mine.regions.subcommands;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.mine.region.AbstractRegionCommand;
import me.catalysmrl.catamines.command.utils.CommandContext;
import me.catalysmrl.catamines.command.utils.CommandException;
import me.catalysmrl.catamines.command.utils.MineTarget;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class RegionResetCommand extends AbstractRegionCommand {
    public RegionResetCommand() {
        super("reset", "catamines.regions.reset", Predicates.equals(0), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine,
            CataMineRegion region) throws CommandException {
        assertArgLength(ctx);

        plugin.getMineManager().resetRegion(region);
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target,
            CataMineRegion region) {
        return Collections.emptyList();
    }

    @Override
    public Message getDescription() {
        return Message.REGIONS_RESET_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.REGIONS_RESET_USAGE;
    }
}
