package me.catalysmrl.catamines.commands.mine.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetTeleportCommand extends AbstractMineCommand {

    public SetTeleportCommand() {
        super("setteleport", "catamines.setteleport", Predicates.equals(1), true);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine) throws CommandException {
        Player player = (Player) sender;
        mine.setTeleportLocation(player.getLocation());
        Messages.sendPrefixed(sender, "&aTeleport location set for mine " + mine.getName());
        requireSave();
    }

    @Override
    public String getDescription() {
        return "Sets the teleport location of a mine";
    }

    @Override
    public String getUsage() {
        return "/cm setteleport <mine>";
    }
}
