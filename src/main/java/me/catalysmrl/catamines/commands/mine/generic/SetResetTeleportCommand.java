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

public class SetResetTeleportCommand extends AbstractMineCommand {

    public SetResetTeleportCommand() {
        super("setresetteleport", "catamines.setresetteleport", Predicates.equals(1), true);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine) throws CommandException {
        Player player = (Player) sender;
        mine.setResetTeleportLocation(player.getLocation());
        mine.setTeleportPlayers(true);
        Messages.sendPrefixed(sender, "&aReset teleport location set for mine " + mine.getName() + " and teleport players enabled.");
        requireSave();
    }

    @Override
    public String getDescription() {
        return "Sets the reset teleport location of a mine";
    }

    @Override
    public String getUsage() {
        return "/cm setresetteleport <mine>";
    }
}
