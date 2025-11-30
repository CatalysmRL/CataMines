package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.Flag;
import me.catalysmrl.catamines.api.mine.PropertyHolder;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.command.utils.MineTarget;
import me.catalysmrl.catamines.mine.components.manager.choice.Identifiable;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Messages;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetTeleportCommand extends AbstractMineCommand {

    public SetTeleportCommand() {
        super("setteleport", "catamines.setteleport", Predicates.equals(1), true);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target)
            throws CommandException {
        Player player = (Player) sender;
        PropertyHolder holder = (PropertyHolder) target.getTarget();
        holder.setFlag(Flag.TELEPORT_LOCATION, player.getLocation());

        String targetName = target.getMine().getName();
        if (holder instanceof Identifiable) {
            targetName = ((Identifiable) holder).getName();
        }

        Messages.sendPrefixed(sender, "&aTeleport location set for " + targetName);
        requireSave();
    }

    @Override
    public Message getDescription() {
        return Message.SETTELEPORT_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.SETTELEPORT_USAGE;
    }
}
