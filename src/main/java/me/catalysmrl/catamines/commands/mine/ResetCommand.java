package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.command.utils.CommandContext;
import me.catalysmrl.catamines.command.utils.CommandException;
import me.catalysmrl.catamines.command.utils.MineTarget;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

public class ResetCommand extends AbstractMineCommand {
    public ResetCommand() {
        super("reset", "catamines.command.reset", Predicates.inRange(0, 1), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target)
            throws CommandException {
        assertArgLength(ctx);

        CataMine mine = target.getMine();

        if (!sender.hasPermission("catamines.command.reset." + mine.getName())) {
            Message.NO_PERMISSION.send(sender);
            return;
        }

        if (target.getTarget() instanceof CataMineRegion region) {
            plugin.getMineManager().resetRegion(region);
        } else {
            mine.reset(plugin);
        }

        Message.RESET_SUCCESS.send(sender, target.toPath());

    }

    @Override
    public Message getDescription() {
        return Message.RESET_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.RESET_USAGE;
    }
}
