package me.catalysmrl.catamines.commands.mine.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

import java.io.IOException;
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
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) {

        int timeToSetInSeconds;
        try {
            timeToSetInSeconds = Integer.parseInt(args.get(0));
        } catch (NumberFormatException e) {
            Message.SET_INVALID_NUMBER.send(sender, args.get(0));
            return;
        }

        if (args.size() == 2) {
            String timeFormat = args.get(1).toLowerCase(Locale.ROOT);
            switch (timeFormat) {
                case "seconds" -> {}
                case "minutes" -> timeToSetInSeconds *= 60;
                case "hours" -> timeToSetInSeconds *= 3600;
                case "days" -> timeToSetInSeconds *= 86400;
                default -> {
                    Message.TIMER_INVALID_FORMAT.send(sender);
                    return;
                }
            }
        }

        mine.getController().setResetDelay(timeToSetInSeconds);
        Message.TIMER_SUCCESS.send(sender);

        try {
            plugin.getMineManager().saveMine(mine);
        } catch (IOException e) {
            Message.MINE_SAVE_EXCEPTION.send(sender);
        }
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getUsage() {
        return "/cm delay <mine> <value> [format]";
    }
}
