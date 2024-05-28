package me.catalysmrl.catamines.commands.mine.regions.subcommands;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegionDeleteCommand extends AbstractMineCommand {
    public RegionDeleteCommand() {
        super("delete", "catamines.regions.delete", i -> i == 2, false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) {
        String regionName = args.get(0);

        mine.getRegionManager().get(regionName)
                .ifPresentOrElse(cataMineRegion -> {
                    mine.getRegionManager().remove(cataMineRegion);
                    Message.REGION_DELETE_SUCCESS.send(sender);

                    try {
                        plugin.getMineManager().saveMine(mine);
                    } catch (IOException e) {
                        Message.MINE_SAVE_EXCEPTION.send(sender, mine.getName());
                    }
                }, () -> Message.REGION_NOT_EXISTS.send(sender));
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) {
        if (args.size() == 1) {
            return StringUtil.copyPartialMatches(args.get(0),
                    mine.getRegionManager().getChoices().stream()
                            .map(CataMineRegion::getName)
                            .toList(), new ArrayList<>());
        } else {
            return Collections.emptyList();
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
