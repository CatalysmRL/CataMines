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

public class DisplayNameCommand extends AbstractMineCommand {
    public DisplayNameCommand() {
        super("displayname", "catamines.displayname", Predicates.any(), false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target)
            throws CommandException {
        CataMine mine = target.getMine();

        String oldDisplayName = mine.getDisplayName();

        String displayName = String.join(" ", ctx.getRemainingArgs());
        mine.setDisplayName(displayName);

        Message.DISPLAYNAME_SUCCESS.send(sender, oldDisplayName, displayName);

        requireSave();
    }

    @Override
    public Message getDescription() {
        return Message.DISPLAYNAME_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.DISPLAYNAME_USAGE;
    }
}
