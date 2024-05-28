package me.catalysmrl.catamines.commands.mine.generic;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.List;

public class DisplayNameCommand extends AbstractMineCommand {
    public DisplayNameCommand() {
        super("displayname", "catamines.displayname", i -> true, false);
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) {

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
