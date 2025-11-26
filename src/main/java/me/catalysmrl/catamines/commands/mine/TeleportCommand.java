package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Messages;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand extends AbstractMineCommand {

    public TeleportCommand() {
        super("teleport", "catamines.teleport", Predicates.inRange(1, 2), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine)
            throws CommandException {
        if (mine.getFlags().getTeleportLocation() == null) {
            Messages.sendPrefixed(sender, "Teleport location not set for this mine!");
            return;
        }

        if (ctx.hasNext()) {
            String playerName = ctx.next();
            Player target = Bukkit.getPlayer(playerName);
            if (target == null) {
                Messages.sendPrefixed(sender, "Player not found: " + playerName);
                return;
            }
            target.teleport(mine.getFlags().getTeleportLocation());
            Messages.sendPrefixed(sender, "Teleported " + playerName + " to mine " + mine.getName());
        } else {
            if (!(sender instanceof Player)) {
                Messages.sendPrefixed(sender, "Only players can use this command!");
                return;
            }
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
