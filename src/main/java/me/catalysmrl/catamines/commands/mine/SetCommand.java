package me.catalysmrl.catamines.commands.mine;

import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.world.block.BaseBlock;
import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.command.utils.MineTarget;
import me.catalysmrl.catamines.mine.components.composition.CataMineBlock;
import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.utils.helper.BlockUtil;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.message.Messages;
import me.catalysmrl.catamines.utils.worldedit.BaseBlockParser;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SetCommand extends AbstractMineCommand {
    public SetCommand() {
        super("set", "catamines.set", Predicates.inRange(1, 4), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target)
            throws CommandException {
        assertArgLength(ctx);

        CataMine mine = target.getMine();

        String baseBlockName = ctx.peek();
        BaseBlock baseBlock;
        try {
            baseBlock = BaseBlockParser.parseInput(ctx.next());
        } catch (InputParseException e) {
            Message.SET_INVALID_BLOCKSTATE.send(sender);
            return;
        }

        double chance = 100d;
        if (ctx.hasNext()) {
            String chanceRaw = ctx.peek().replace("%", "");
            try {
                chance = Double.parseDouble(chanceRaw);
                ctx.next();
            } catch (NumberFormatException e) {
                Message.SET_INVALID_NUMBER.send(sender, ctx.next());
                return;
            }

            if (chance < 0 || chance > 100) {
                Message.SET_INVALID_CHANCE.send(sender);
                return;
            }
        }

        CataMineRegion region = target.getRegion();
        if (region == null) {
            Optional<CataMineRegion> regionOptional = mine.getRegionManager().get("default");
            if (regionOptional.isPresent()) {
                region = regionOptional.get();
            } else {
                Message.SET_INVALID_REGION.send(sender);
                return;
            }
        }

        CataMineComposition composition = target.getComposition();
        if (composition == null) {
            Optional<CataMineComposition> compositionOptional = region.getCompositionManager().get("default");
            if (compositionOptional.isPresent()) {
                composition = compositionOptional.get();
            } else {
                Message.SET_INVALID_COMPOSITION.send(sender);
                return;
            }
        }

        CataMineBlock block = new CataMineBlock(baseBlock, chance);
        composition.addBlock(block);

        Message.SET_SUCCESS.send(sender, mine.getName(), baseBlockName, block.getChance(), region.getName(),
                composition.getName(), 100 - composition.getChanceSum());

        requireSave();
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext context, CataMine mine) {
        List<String> remainingArgs = context.getRemainingArgs();
        switch (remainingArgs.size()) {
            case 1 -> {
                return StringUtil.copyPartialMatches(remainingArgs.get(0), BlockUtil.getAllPlaceableBlockNames(),
                        new ArrayList<>());
            }
            case 2 -> {
                return StringUtil.copyPartialMatches(remainingArgs.get(1), Collections.singletonList("%"),
                        new ArrayList<>());
            }
            case 3 -> {
                List<String> regionNames = mine.getRegionManager().getChoices()
                        .stream()
                        .map(CataMineRegion::getName)
                        .toList();
                return StringUtil.copyPartialMatches(remainingArgs.get(2), regionNames, new ArrayList<>());
            }
            case 4 -> {
                Optional<CataMineRegion> region = mine.getRegionManager().get(remainingArgs.get(2));
                if (region.isEmpty())
                    return Collections.singletonList(Messages.colorize("&cUnknown region"));

                List<String> compositionNames = region.get().getCompositionManager().getChoices()
                        .stream()
                        .map(CataMineComposition::getName)
                        .toList();

                return StringUtil.copyPartialMatches(remainingArgs.get(3), compositionNames, new ArrayList<>());
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public Message getDescription() {
        return Message.SET_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.SET_USAGE;
    }
}
