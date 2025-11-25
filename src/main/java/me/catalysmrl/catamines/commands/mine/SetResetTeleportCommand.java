package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Messages;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetResetTeleportCommand extends AbstractMineCommand {

    public SetResetTeleportCommand() {
        super("setresetteleport", "catamines.setresetteleport", Predicates.equals(1), true);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine)
            throws CommandException {
        Player player = (Player) sender;
        mine.setResetTeleportLocation(player.getLocation());
        mine.setTeleportPlayers(true);
        Messages.sendPrefixed(sender,
                "&aReset teleport location set for mine " + mine.getName() + " and teleport players enabled.");
        requireSave();
    }

    @Override
    public Message getDescription() {
        return Message.SETRESETTELEPORT_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.SETRESETTELEPORT_USAGE;
    }
}
