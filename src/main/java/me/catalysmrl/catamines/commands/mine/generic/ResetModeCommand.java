package me.catalysmrl.catamines.commands.mine.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.mine.components.manager.controller.CataMineController;
import me.catalysmrl.catamines.utils.helper.Predicates;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.List;

public class ResetModeCommand extends AbstractMineCommand {
    public ResetModeCommand() {
        super("resetmode", "catamines.resetmode", Predicates.inRange(1, 1), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) {

        CataMineController.ResetMode resetMode;

        try {
            resetMode = CataMineController.ResetMode.valueOf(args.get(0));
        } catch (IllegalArgumentException e) {
            Message.RESETMODE_INVALID.send(sender);
            return;
        }

        mine.getController().setResetMode(resetMode);

        try {
            plugin.getMineManager().saveMine(mine);
        } catch (IOException e) {
            Message.MINE_SAVE_EXCEPTION.send(sender);
        }
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getUsage() {
        return "";
    }
}
