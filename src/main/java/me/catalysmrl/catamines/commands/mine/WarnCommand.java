package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.command.utils.MineTarget;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class WarnCommand extends AbstractMineCommand {

    public WarnCommand() {
        super("warn", "catamines.warn", Predicates.inRange(2, 3), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target)
            throws CommandException {

    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine) {
        if (ctx.remaining() == 1) {
            return Arrays.asList("enable", "distance", "global", "hotbar", "seconds", "true", "false");
        }
        return super.tabComplete(plugin, sender, ctx, mine);
    }

    @Override
    public Message getDescription() {
        return Message.WARN_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.WARN_USAGE;
    }
}
