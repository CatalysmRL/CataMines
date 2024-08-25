package me.catalysmrl.catamines.command.abstraction.mine.region;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.message.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class AbstractRegionCommand extends AbstractMineCommand {

    public AbstractRegionCommand(String name, String permission, Predicate<Integer> argumentCheck, boolean onlyPlayers) {
        super(name, permission, argumentCheck, onlyPlayers);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) {
        String regionName = args.get(0);

        Optional<CataMineRegion> regionOptional = mine.getRegionManager().get(regionName);

        if (regionOptional.isEmpty()) {
            Message.REGION_NOT_EXISTS.send(sender, regionName);
            return;
        }

        execute(plugin, sender, args.subList(1, args.size()), mine, regionOptional.get());
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) {
        if (args.size() == 1) {
            return StringUtil.copyPartialMatches(args.get(0),
                    mine.getRegionManager().getChoices().stream()
                            .map(CataMineRegion::getName)
                            .toList(), new ArrayList<>());
        } else {
            Optional<CataMineRegion> regionOptional = mine.getRegionManager().get(args.get(0));
            if (regionOptional.isEmpty()) return List.of(Messages.colorize("&cUnknown region"));
            return tabComplete(plugin, sender, args.subList(1, args.size()), mine, regionOptional.get());
        }
    }

    public abstract void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine, CataMineRegion region);

    public List<String> tabComplete(CataMines plugin, CommandSender sender, List<String> args, CataMine mine, CataMineRegion region) {
        return Collections.emptyList();
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getUsage() {
        return "";
    }
}
