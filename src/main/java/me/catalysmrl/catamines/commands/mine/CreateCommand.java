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
import me.catalysmrl.catamines.mine.mines.AdvancedCataMine;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.undo.UndoManager;
import me.catalysmrl.catamines.utils.worldedit.WorldEditUtils;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.regions.RegionSelector;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Collections;

public class CreateCommand extends AbstractCommand {

    public CreateCommand() {
        super("create", "catamines.create", Predicates.inRange(1, 2), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx) throws CommandException {
        assertArgLength(ctx);

        String pathTarget = ctx.next();
        String[] parts = pathTarget.split(":", 3);
        String mineName = parts[0];

        if ("*".equals(mineName)) {
            Message.CREATE_INVALID_NAME.send(sender);
            return;
        }

        // Case 1: Mine does not exist -> Create Mine + Region + Composition
        if (!plugin.getMineManager().containsMine(mineName)) {
            createMine(plugin, sender, ctx, mineName, parts);
            return;
        }

        CataMine mine = plugin.getMineManager().getMine(mineName).orElseThrow();

        // Case 2: Mine exists, check for Region
        if (parts.length < 2) {
            Message.CREATE_MINE_EXISTS.send(sender, mineName);
            return;
        }

        String regionName = parts[1];
        if ("*".equals(regionName)) {
            Message.CREATE_INVALID_NAME.send(sender);
            return;
        }

        // Case 3: Region does not exist -> Create Region + Composition
        if (!mine.getRegionManager().contains(regionName)) {
            createRegion(plugin, sender, ctx, mine, regionName, parts);
            return;
        }

        CataMineRegion region = mine.getRegionManager().get(regionName).orElseThrow();

        // Case 4: Region exists, check for Composition
        if (parts.length < 3) {
            Message.CREATE_REGION_EXISTS.send(sender, regionName);
            return;
        }

        String compName = parts[2];
        if ("*".equals(compName)) {
            Message.CREATE_INVALID_NAME.send(sender);
            return;
        }

        // Case 5: Composition does not exist -> Create Composition
        if (!region.getCompositionManager().contains(compName)) {
            createComposition(plugin, sender, mine, region, compName);
            return;
        }

        // Case 6: Composition exists
        Message.CREATE_COMPOSITION_EXISTS.send(sender, compName);
    }

    private void createMine(CataMines plugin, CommandSender sender, CommandContext ctx, String mineName, String[] parts) {
        String regionName = parts.length > 1 ? parts[1] : "default";
        String compName = parts.length > 2 ? parts[2] : "default";

        CataMine cataMine = new AdvancedCataMine(plugin, mineName);
        
        if (createRegionComponents(sender, ctx, cataMine, regionName, compName)) {
            UndoManager.recordCreation(sender, mineName);
            plugin.getMineManager().registerMine(cataMine);
            Message.CREATE_SUCCESS.send(sender, mineName);
            saveMine(plugin, sender, cataMine);
        }
    }

    private void createRegion(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine, String regionName, String[] parts) {
        String compName = parts.length > 2 ? parts[2] : "default";

        if (createRegionComponents(sender, ctx, mine, regionName, compName)) {
            UndoManager.record(sender, Collections.singletonList(mine), "create region " + regionName);
            Message.CREATE_REGION_SUCCESS.send(sender, regionName, mine.getName());
            saveMine(plugin, sender, mine);
        }
    }

    private boolean createRegionComponents(CommandSender sender, CommandContext ctx, CataMine mine, String regionName, String compName) {
        if (!(sender instanceof Player player)) {
            Message.ONLY_PLAYERS.send(sender);
            return false;
        }

        RegionSelector regionSelector = WorldEditUtils.getSelector(player);
        CataMineRegion region;

        if (!ctx.hasNext()) {
            // No schematic provided, use selection
            if (regionSelector.isDefined()) {
                region = new SelectionRegion(regionName, regionSelector);
            } else {
                Message.CREATE_INCOMPLETE_REGION.send(sender);
                return false;
            }
        } else {
            // Schematic provided
            try {
                region = new SchematicRegion(regionName, ctx.peek(), regionSelector);
            } catch (IncompleteRegionException e) {
                Message.CREATE_INCOMPLETE_REGION.send(sender);
                return false;
            } catch (IllegalArgumentException e) {
                Message.CREATE_INVALID_SCHEMATIC.send(sender, ctx.peek());
                return false;
            }
        }

        region.getCompositionManager().add(new CataMineComposition(compName));
        mine.getRegionManager().add(region);
        return true;
    }

    private void createComposition(CataMines plugin, CommandSender sender, CataMine mine, CataMineRegion region, String compName) {
        UndoManager.record(sender, Collections.singletonList(mine), "create composition " + compName);
        region.getCompositionManager().add(new CataMineComposition(compName));
        Message.CREATE_COMPOSITION_SUCCESS.send(sender, compName, region.getName(), mine.getName());
        saveMine(plugin, sender, mine);
    }

    private void saveMine(CataMines plugin, CommandSender sender, CataMine mine) {
        try {
            plugin.getMineManager().saveMine(mine);
        } catch (IOException e) {
            Message.MINE_SAVE_EXCEPTION.send(sender, mine.getName());
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