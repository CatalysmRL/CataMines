package me.catalysmrl.catamines.commands.mine;

import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.world.block.BlockState;
import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.AbstractCataMineCommand;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.mine.abstraction.CataMine;
import me.catalysmrl.catamines.mine.components.composition.CataMineBlock;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SetCommand extends AbstractCataMineCommand {
    public SetCommand() {
        super("set", "catamines.command.set", Predicates.inRange(2, 3), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) throws CommandException {

        BlockState blockState;
        try {
            blockState = BlockState.get(args.get(0));
        } catch (InputParseException e) {
            Message.SET_INVALID_BLOCKSTATE.send(sender);
            return;
        }

        double chance = 100d;
        if (args.size() == 2) {
            try {
                chance = Double.parseDouble(args.get(1).replace("%", ""));
            } catch (NumberFormatException e) {
                Message.SET_INVALID_NUMBER.send(sender, args.get(1));
                return;
            }

            if (chance < 0 || chance > 100) {
                Message.SET_INVALID_CHANCE.send(sender);
                return;
            }
        }

        CataMineBlock block = new CataMineBlock(blockState.toBaseBlock(), chance);

        mine.getRegions().get(0).getCompositions().get(0).add(block);

        Message.SET_SUCCESS.send(sender, args.get(0), block.getChance(), mine.getName());
    }

    @Override
    public String getDescription() {
        return "Sets a block into the composition";
    }

    @Override
    public String getUsage() {
        return "/cm set <mine> <blockData> (%)";
    }
}
