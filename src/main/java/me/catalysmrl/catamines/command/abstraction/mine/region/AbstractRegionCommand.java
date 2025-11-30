package me.catalysmrl.catamines.command.abstraction.mine.region;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.message.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class AbstractRegionCommand extends AbstractMineCommand {

    public AbstractRegionCommand(String name, String permission, Predicate<Integer> argumentCheck,
            boolean onlyPlayers) {
        super(name, permission, argumentCheck, onlyPlayers);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx,
            me.catalysmrl.catamines.command.utils.MineTarget target)
            throws CommandException {
        CataMine mine = target.getMine();
        CataMineRegion region;

        if (target.getTarget() instanceof CataMineRegion) {
            region = (CataMineRegion) target.getTarget();
        } else {
            if (!ctx.hasNext()) {
                // If no region specified in args, and target is mine, show usage or error?
                // The original code expected a region name argument.
                // We'll assume if it's not in target, it MUST be in args.
                throw new me.catalysmrl.catamines.command.utils.ArgumentException.Usage();
            }
            String regionName = ctx.next();
            Optional<CataMineRegion> regionOptional = mine.getRegionManager().get(regionName);

            if (regionOptional.isEmpty()) {
                Message.REGIONS_NOT_EXISTS.send(sender, regionName);
                return;
            }
            region = regionOptional.get();
        }

        execute(plugin, sender, ctx, mine, region);
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine) {
        if (ctx.remaining() == 1) {
            return StringUtil.copyPartialMatches(ctx.peek(),
                    mine.getRegionManager().getChoices().stream()
                            .map(CataMineRegion::getName)
                            .toList(),
                    new ArrayList<>());
        } else {
            Optional<CataMineRegion> regionOptional = mine.getRegionManager().get(ctx.peek());
            if (regionOptional.isEmpty())
                return List.of(Messages.colorize("&cUnknown region"));
            ctx.next();
            return tabComplete(plugin, sender, ctx, mine, regionOptional.get());
        }
    }

    public abstract void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine,
            CataMineRegion region) throws CommandException;

    public abstract List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine,
            CataMineRegion region);

}
