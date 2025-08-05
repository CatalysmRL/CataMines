package me.catalysmrl.catamines.commands.mine.regions.subcommands;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.region.AbstractRegionCommand;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.LegacyMessage;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class RegionDeleteCommand extends AbstractRegionCommand {
    public RegionDeleteCommand() {
        super("delete", "catamines.regions.delete", Predicates.equals(0), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine, CataMineRegion region) throws CommandException {
        assertArgLength(ctx);

        mine.getRegionManager().remove(region);
        LegacyMessage.REGION_DELETE_SUCCESS.send(sender);

        requireSave();
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine, CataMineRegion region) {
        return Collections.emptyList();
    }

    @Override
    public String getDescription() {
        return LegacyMessage.REGION_DELETE_DESCRIPTION.getMessage();
    }

    @Override
    public String getUsage() {
        return "/cm regions <mine> delete <name>";
    }
}
