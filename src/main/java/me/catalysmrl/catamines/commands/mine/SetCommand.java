package me.catalysmrl.catamines.commands.mine;

import com.sk89q.worldedit.extension.input.InputParseException;
import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.command.utils.CommandContext;
import me.catalysmrl.catamines.command.utils.CommandException;
import me.catalysmrl.catamines.command.utils.MineTarget;
import me.catalysmrl.catamines.mine.components.composition.CataMineBlock;
import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.utils.helper.BlockUtil;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class SetCommand extends AbstractMineCommand {

    public SetCommand() {
        super("set", "catamines.set", Predicates.inRange(1, 2), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target) throws CommandException {
        assertArgLength(ctx);

        target.resolveDefaults();
        CataMine mine = target.getMine();
        CataMineRegion region = target.getRegion();
        CataMineComposition composition = target.getComposition();

        if (region == null || composition == null) {
            Message.INVALID_TARGET.send(sender, target.toPath());
            return;
        }

        String blockInput = ctx.next();

        double chance = 100.0;
        if (ctx.hasNext()) {
            String chanceStr = ctx.next().replace("%", "").trim();
            try {
                chance = Double.parseDouble(chanceStr);
            } catch (NumberFormatException e) {
                Message.SET_INVALID_NUMBER.send(sender, chanceStr);
                return;
            }

            if (chance < 0 || chance > 100) {
                Message.SET_INVALID_CHANCE.send(sender);
                return;
            }
        }

        CataMineBlock block;
        try {
            block = new CataMineBlock(blockInput, chance);
        } catch (InputParseException e) {
            Message.SET_INVALID_BLOCKSTATE.send(sender);
            e.printStackTrace();
            return;
        } catch (IllegalArgumentException e) {
            Message.SET_INVALID_BLOCKSTATE.send(sender);
            e.printStackTrace();
            return;
        }
        composition.addBlock(block);

        double remaining = 100.0 - composition.getChanceSum();

        Message.SET_SUCCESS.send(sender,
                mine.getName(),
                blockInput,
                chance,
                region.getName(),
                composition.getName(),
                remaining > 0 ? "&a" + String.format("%.2f", remaining) : "&c" + String.format("%.2f", remaining));

        requireSave();
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target) {
        List<String> completions = new ArrayList<>();

        if (ctx.remaining() == 1) {
            // First argument: block
            String input = ctx.peek() != null ? ctx.peek() : "";
            StringUtil.copyPartialMatches(input, BlockUtil.getAllPlaceableBlockNames(), completions);
        } else if (ctx.remaining() == 2) {
            // Second argument: chance
            String input = ctx.peek() != null ? ctx.peek() : "";
            // Suggest common values
            List.of("100", "50", "25", "10", "5", "1").stream()
                    .map(s -> input.contains("%") ? s + "%" : s + "%")
                    .forEach(completions::add);
        }

        return completions;
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