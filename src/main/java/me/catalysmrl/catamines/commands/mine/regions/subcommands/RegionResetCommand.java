package me.catalysmrl.catamines.commands.mine.regions.subcommands;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.mine.region.AbstractRegionCommand;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import org.bukkit.command.CommandSender;

import java.util.List;

public class RegionResetCommand extends AbstractRegionCommand {
    public RegionResetCommand() {
        super("reset", "catamines.regions.reset", integer -> integer == 2, false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine, CataMineRegion region) {
        plugin.getMineManager().resetRegion(region);
    }
}
