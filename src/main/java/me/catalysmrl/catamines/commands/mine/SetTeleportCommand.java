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

public class SetTeleportCommand extends AbstractMineCommand {

    public SetTeleportCommand() {
        super("setteleport", "catamines.setteleport", Predicates.equals(1), true);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine)
            throws CommandException {
        Player player = (Player) sender;
        mine.setTeleportLocation(player.getLocation());
        Messages.sendPrefixed(sender, "&aTeleport location set for mine " + mine.getName());
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
