package me.catalysmrl.catamines.commands.mine;

import com.sk89q.worldedit.regions.RegionSelector;
import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.AbstractCataCommand;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.mine.abstraction.CataMine;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.mine.components.region.impl.SelectionRegion;
import me.catalysmrl.catamines.mine.mines.AdvancedCataMine;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.worldedit.WorldEditUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;

public class CreateCommand extends AbstractCataCommand {

    public CreateCommand() {
        super("create", "catamines.command.create", integer -> integer == 1, true);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) throws CommandException {
        String name = args.get(0);

        if ("*".equals(name)) {
            Message.MINE_INVALID_NAME.send(sender);
            return;
        }

        if (plugin.getMineManager().containsMine(name)) {
            Message.MINE_EXIST.send(sender, name);
            return;
        }

        Player player = (Player) sender;

        RegionSelector regionSelector = WorldEditUtils.getSelector(player);

        CataMine cataMine = new AdvancedCataMine(name);

        if (regionSelector.isDefined()) {
            CataMineRegion region = new SelectionRegion("default", regionSelector);
            cataMine.getRegions().add(region);
        }

        plugin.getMineManager().registerMine(cataMine);
        Message.CREATE_SUCCESS.send(player, name);

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
