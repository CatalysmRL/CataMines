package me.catalysmrl.catamines.commands.mine.generic;

import com.sk89q.worldedit.regions.RegionSelector;
import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.AbstractCommand;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.mine.components.region.impl.SelectionRegion;
import me.catalysmrl.catamines.mine.mines.AdvancedCataMine;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.worldedit.WorldEditUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CreateCommand extends AbstractCommand {

    public CreateCommand() {
        super("create", "catamines.create", Predicates.equals(1), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx) throws CommandException {
        assertArgLength(ctx);

        String name = ctx.peek();

        if ("*".equals(name)) {
            Message.MINE_INVALID_NAME.send(sender);
            return;
        }

        if (plugin.getMineManager().containsMine(name)) {
            Message.MINE_EXISTS.send(sender, name);
            return;
        }

        CataMine cataMine = new AdvancedCataMine(plugin, name);

        if (sender instanceof Player player) {
            RegionSelector regionSelector = WorldEditUtils.getSelector(player);

            if (regionSelector.isDefined()) {
                CataMineRegion region = new SelectionRegion("default", regionSelector);
                region.getCompositionManager().add(new CataMineComposition("default"));
                cataMine.getRegionManager().add(region);
            } else {
                Message.INCOMPLETE_REGION.send(sender);
            }
        }


        plugin.getMineManager().registerMine(cataMine);
        Message.CREATE_SUCCESS.send(sender, name);

        try {
            plugin.getMineManager().saveMine(cataMine);
        } catch (IOException e) {
            Message.MINE_SAVE_EXCEPTION.send(sender, name);
        }
    }

    @Override
    public String getDescription() {
        return Message.CREATE_DESCRIPTION.getKey();
    }

    @Override
    public String getUsage() {
        return "/cm create <mine>";
    }
}
