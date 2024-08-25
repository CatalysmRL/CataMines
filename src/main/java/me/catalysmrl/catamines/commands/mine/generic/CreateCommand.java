package me.catalysmrl.catamines.commands.mine.generic;

import com.sk89q.worldedit.regions.RegionSelector;
import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.AbstractCommand;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.mine.components.region.impl.SelectionRegion;
import me.catalysmrl.catamines.mine.mines.AdvancedCataMine;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.worldedit.WorldEditUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;

public class CreateCommand extends AbstractCommand {

    public CreateCommand() {
        super("create", "catamines.create", integer -> integer == 1, false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args) throws CommandException {
        String name = args.get(0);

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
        return Message.CREATE_DESCRIPTION.getMessage();
    }

    @Override
    public String getUsage() {
        return "/cm create <mine>";
    }
}
