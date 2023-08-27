package me.catalysmrl.catamines.commands.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.AbstractCataMineCommand;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.mine.abstraction.CataMine;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.List;

public class DisplayNameCommand extends AbstractCataMineCommand {
    public DisplayNameCommand() {
        super("displayname", "catamines.command.displayname", i -> true, false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) throws CommandException {

        String oldDisplayName = mine.getDisplayName();

        String displayName = String.join(" ", args);
        mine.setDisplayName(displayName);

        Message.DISPLAYNAME_SUCCESS.send(sender, oldDisplayName, displayName);

        try {
            plugin.getMineManager().saveMine(mine);
        } catch (IOException e) {
            Message.MINE_SAVE_EXCEPTION.send(sender, mine.getName());
        }
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }
}
