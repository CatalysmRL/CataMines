package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.AbstractCommand;
import me.catalysmrl.catamines.command.utils.CommandContext;
import me.catalysmrl.catamines.command.utils.CommandException;
import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.mine.components.region.impl.SchematicRegion;
import me.catalysmrl.catamines.mine.components.region.impl.SelectionRegion;
import me.catalysmrl.catamines.mine.mines.CataMine;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.worldedit.WorldEditUtils;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.regions.RegionSelector;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CreateCommand extends AbstractCommand {

    public CreateCommand() {
        super("create", "catamines.create", Predicates.inRange(1, 2), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx) throws CommandException {
        assertArgLength(ctx);

        String name = ctx.next();

        if ("*".equals(name)) {
            Message.MINE_INVALID_NAME.send(sender);
            return;
        }

        if (plugin.getMineManager().containsMine(name)) {
            Message.MINE_EXISTS.send(sender, name);
            return;
        }

        CataMine cataMine = new CataMine(plugin, name);

        if (sender instanceof Player player) {
            RegionSelector regionSelector = WorldEditUtils.getSelector(player);

            if (!ctx.hasNext()) {
                if (regionSelector.isDefined()) {
                    CataMineRegion region = new SelectionRegion("default", regionSelector);
                    region.getCompositionManager().add(new CataMineComposition("default"));
                    cataMine.getRegionManager().add(region);
                } else {
                    Message.INCOMPLETE_REGION.send(sender);
                }
            } else {
                try {
                    CataMineRegion region = new SchematicRegion("default", ctx.peek(), regionSelector);
                    region.getCompositionManager().add(new CataMineComposition("default"));
                    cataMine.getRegionManager().add(region);
                } catch (IncompleteRegionException e) {
                    Message.INCOMPLETE_REGION.send(sender);
                } catch (IllegalArgumentException e) {
                    Message.INVALID_SCHEMATIC.send(sender, ctx.peek());
                }
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
    public Message getDescription() {
        return Message.CREATE_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.CREATE_USAGE;
    }
}