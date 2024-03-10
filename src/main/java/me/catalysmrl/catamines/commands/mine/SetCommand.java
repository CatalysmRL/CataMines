package me.catalysmrl.catamines.commands.mine;

import com.sk89q.worldedit.world.block.BaseBlock;
import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.AbstractCataMineCommand;
import me.catalysmrl.catamines.mine.components.composition.CataMineBlock;
import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.worldedit.BaseBlockParser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Optional;

public class SetCommand extends AbstractCataMineCommand {
    public SetCommand() {
        super("set", "catamines.command.set", Predicates.inRange(2, 5), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) {

        Bukkit.broadcastMessage(args.toString());

        BaseBlock baseBlock;
        try {
            baseBlock = BaseBlockParser.asBaseBlock(args.get(0));
        } catch (Exception e) {
            Message.SET_INVALID_BLOCKSTATE.send(sender);
            return;
        }

        double chance = 100d;
        if (args.size() >= 2) {
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

        String regionName = "default";
        if (args.size() >= 3) {
            regionName = args.get(2);
        }

        String compositionName = "default";
        if (args.size() >= 4) {
            compositionName = args.get(3);
        }

        Optional<CataMineRegion> regionOptional = mine.getRegionManager().get(regionName);
        if (regionOptional.isEmpty()) {
            Message.SET_INVALID_REGION.send(sender);
            return;
        }

        CataMineRegion region = regionOptional.get();

        Optional<CataMineComposition> compositionOptional = region.getCompositionManager().get(compositionName);
        if (compositionOptional.isEmpty()) {
            Message.SET_INVALID_COMPOSITION.send(sender);
            return;
        }

        CataMineComposition composition = compositionOptional.get();

        CataMineBlock block = new CataMineBlock(baseBlock, chance);

        composition.add(block);

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
