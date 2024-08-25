package me.catalysmrl.catamines.commands.mine.regions.subcommands;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.mine.region.AbstractRegionCommand;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.List;

public class RegionDeleteCommand extends AbstractRegionCommand {
    public RegionDeleteCommand() {
        super("delete", "catamines.regions.delete", i -> i == 2, false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine, CataMineRegion region) {
        mine.getRegionManager().remove(region);
        Message.REGION_DELETE_SUCCESS.send(sender);
        try {
            plugin.getMineManager().saveMine(mine);
        } catch (IOException e) {
            Message.MINE_SAVE_EXCEPTION.send(sender, mine.getName());
        }
    }

    @Override
    public String getDescription() {
        return Message.REGION_DELETE_DESCRIPTION.getMessage();
    }

    @Override
    public String getUsage() {
        return "/cm regions <mine> delete <name>";
    }
}
