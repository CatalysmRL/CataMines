package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class TimerCommand extends AbstractMineCommand {

    public TimerCommand() {
        super("timer", "catamines.timer", Predicates.inRange(1, 2), false);
    }

    @Override
    public List<String> getAliases() {
        return List.of("delay", "setdelay");
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine)
            throws CommandException {
        assertArgLength(ctx);

        int timeToSetInSeconds;
        try {
            timeToSetInSeconds = Integer.parseInt(ctx.next());
        } catch (NumberFormatException e) {
            Message.SET_INVALID_NUMBER.send(sender, ctx.peek());
            return;
        }

        if (ctx.hasNext()) {
            String timeFormat = ctx.peek().toLowerCase(Locale.ROOT);
            switch (timeFormat) {
                case "seconds", "s" -> {
                }
                case "minutes", "m" -> timeToSetInSeconds *= 60;
                case "hours", "h" -> timeToSetInSeconds *= 3600;
                case "days", "d" -> timeToSetInSeconds *= 86400;
                default -> {
                    Message.TIMER_INVALID_FORMAT.send(sender);
                    return;
                }
            }
        }

        mine.getController().setResetDelay(timeToSetInSeconds);
        Message.TIMER_SUCCESS.send(sender);

        requireSave();
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine) {
        switch (ctx.remaining()) {
            case 1 -> {
                return List.of("Number");
            }
            case 2 -> {
                return List.of("seconds", "minutes", "hours", "days", "s", "m", "h", "d");
            }

            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public Message getDescription() {
        return Message.TIMER_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.TIMER_USAGE;
    }
}
