package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.Flag;
import me.catalysmrl.catamines.api.mine.PropertyHolder;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.command.utils.CommandContext;
import me.catalysmrl.catamines.command.utils.CommandException;
import me.catalysmrl.catamines.command.utils.MineTarget;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Messages;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand extends AbstractMineCommand {

    public TeleportCommand() {
        super("teleport", "catamines.teleport", Predicates.inRange(1, 2), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target)
            throws CommandException {
        PropertyHolder holder = (PropertyHolder) target.getTarget();
        Location loc = holder.getFlag(Flag.TELEPORT_LOCATION);

        if (loc == null) {
            Messages.sendPrefixed(sender, "Teleport location not set for this target!");
            return;
        }

        if (ctx.hasNext()) {
            String playerName = ctx.next();
            Player targetPlayer = Bukkit.getPlayer(playerName);
            if (targetPlayer == null) {
                Messages.sendPrefixed(sender, "Player not found: " + playerName);
                return;
            }
            targetPlayer.teleport(loc);
            Messages.sendPrefixed(sender, "Teleported " + playerName + " to " + target.getMine().getName());
        } else {
            if (!(sender instanceof Player)) {
                Messages.sendPrefixed(sender, "Only players can use this command!");
                return;
            }
            ((Player) sender).teleport(loc);
        }
    }

    @Override
    public Message getDescription() {
        return Message.TELEPORT_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.TELEPORT_USAGE;
    }
}
