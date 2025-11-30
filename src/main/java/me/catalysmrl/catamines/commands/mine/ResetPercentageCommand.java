package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.command.utils.MineTarget;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Messages;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

public class ResetPercentageCommand extends AbstractMineCommand {

    public ResetPercentageCommand() {
        super("resetpercentage", "catamines.resetpercentage", Predicates.equals(1), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target)
            throws CommandException {
        assertArgLength(ctx);

        CataMine mine = target.getMine();

        String percentageRaw = ctx.next().replace("%", "");
        double percentage;
        try {
            percentage = Double.parseDouble(percentageRaw);
        } catch (NumberFormatException e) {
            Messages.sendPrefixed(sender, "Invalid number: " + percentageRaw);
            return;
        }

        if (percentage < 0 || percentage > 100) {
            Messages.sendPrefixed(sender, "Invalid percentage: " + percentage);
            return;
        }

        mine.getController().setResetPercentage(percentage);
        Messages.sendPrefixed(sender, "Reset percentage set to " + percentage + "% for mine " + mine.getName());
        requireSave();
    }

    @Override
    public Message getDescription() {
        return Message.RESETPERCENTAGE_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.RESETPERCENTAGE_USAGE;
    }
}
