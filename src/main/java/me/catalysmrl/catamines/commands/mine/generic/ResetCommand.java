package me.catalysmrl.catamines.commands.mine.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.LegacyMessage;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ResetCommand extends AbstractMineCommand {
    public ResetCommand() {
        super("reset", "catamines.command.reset", Predicates.inRange(0, 1), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine) throws CommandException {
        assertArgLength(ctx);

        if (!sender.hasPermission("catamines.command.reset." + mine.getName())) {
            Message.NO_PERMISSION.send(sender);
            return;
        }

        // TODO: Silent resetting and other flags
        mine.reset(plugin);
        LegacyMessage.RESET_SUCCESS.send(sender, mine.getName());
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx) {
        if (ctx.remaining() == 1) {
            List<String> authorizedMineNames = plugin.getMineManager().getMineList().stream()
                    .filter(mineName -> sender.hasPermission("catamines.command.reset." + mineName))
                    .toList();

            return StringUtil.copyPartialMatches(ctx.peek(), authorizedMineNames, new ArrayList<>());
        }

        String mineId = ctx.peek();
        Optional<CataMine> optionalCataMine = plugin.getMineManager().getMine(mineId);
        if (optionalCataMine.isEmpty())
            return Collections.singletonList(Message.of("command.mine-not-exists").format(sender));

        ctx.next();

        return tabComplete(plugin, sender, ctx, optionalCataMine.get());
    }

    @Override
    public String getDescription() {
        return LegacyMessage.RESET_DESCRIPTION.getMessage();
    }

    @Override
    public String getUsage() {
        return "/cm reset <mine> (-s)";
    }
}
