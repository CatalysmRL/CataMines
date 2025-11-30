package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.Flag;
import me.catalysmrl.catamines.api.mine.PropertyHolder;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.command.utils.CommandContext;
import me.catalysmrl.catamines.command.utils.CommandException;
import me.catalysmrl.catamines.command.utils.MineTarget;
import me.catalysmrl.catamines.mine.components.manager.choice.Identifiable;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetResetTeleportCommand extends AbstractMineCommand {

    public SetResetTeleportCommand() {
        super("setresetteleport", "catamines.setresetteleport", Predicates.equals(1), true);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target)
            throws CommandException {
        Player player = (Player) sender;
        PropertyHolder holder = (PropertyHolder) target.getTarget();
        holder.setFlag(Flag.RESET_TELEPORT_LOCATION, player.getLocation());

        String targetName = target.getMine().getName();
        if (holder instanceof Identifiable) {
            targetName = ((Identifiable) holder).getName();
        }

        Message.SETRESETTELEPORT_SUCCESS.send(sender, targetName);
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
