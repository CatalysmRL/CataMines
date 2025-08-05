package me.catalysmrl.catamines.command.abstraction.mine.region;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.utils.message.LegacyMessage;
import me.catalysmrl.catamines.utils.message.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class AbstractRegionCommand extends AbstractMineCommand {

    public AbstractRegionCommand(String name, String permission, Predicate<Integer> argumentCheck, boolean onlyPlayers) {
        super(name, permission, argumentCheck, onlyPlayers);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine) throws CommandException {
        String regionName = ctx.next();

        Optional<CataMineRegion> regionOptional = mine.getRegionManager().get(regionName);

        if (regionOptional.isEmpty()) {
            LegacyMessage.REGION_NOT_EXISTS.send(sender, regionName);
            return;
        }

        execute(plugin, sender, ctx, mine, regionOptional.get());
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine) {
        if (ctx.remaining() == 1) {
            return StringUtil.copyPartialMatches(ctx.peek(),
                    mine.getRegionManager().getChoices().stream()
                            .map(CataMineRegion::getName)
                            .toList(), new ArrayList<>());
        } else {
            Optional<CataMineRegion> regionOptional = mine.getRegionManager().get(ctx.peek());
            if (regionOptional.isEmpty()) return List.of(Messages.colorize("&cUnknown region"));
            ctx.next();
            return tabComplete(plugin, sender, ctx, mine, regionOptional.get());
        }
    }

    public abstract void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine, CataMineRegion region) throws CommandException;

    public abstract List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine, CataMineRegion region);

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getUsage() {
        return "";
    }
}
