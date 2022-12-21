package me.catalysmrl.catamines.command.abstraction;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.utils.ArgumentException;
import me.catalysmrl.catamines.mine.abstraction.CataMine;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.function.Predicate;

public abstract class AbstractCataMineCommand extends AbstractCataCommand {

    public AbstractCataMineCommand(String name, String permission, Predicate<Integer> argumentCheck, boolean onlyPlayers) {
        super(name, permission, argumentCheck, onlyPlayers);
    }

    @Override
    public final void execute(CataMines plugin, CommandSender sender, List<String> args) throws CommandException {

        if (args.size() == 0) {
            throw new ArgumentException.Usage();
        }

        if (args.get(0).equals("*")) {
            if (!sender.hasPermission("catamines.admin")) {
                Message.NO_PERMISSION.send(sender);
                return;
            }

            List<CataMine> mines = plugin.getMineManager().getMines();
            int size = mines.size();
            List<String> strippedArgs = args.subList(1, args.size());

            // Hacky way to avoid ConcurrentModificationException
            // for delete command :/
            for (int i = size - 1; i >= 0; i--) {
                execute(plugin, sender, strippedArgs, mines.get(i));
            }

            sender.sendMessage("");
            Message.QUERY_ALL.send(sender, size);
            sender.sendMessage("");
            return;
        }

        CataMine cataMine = plugin.getMineManager().getMine(args.get(0));
        if (cataMine == null) {
            Message.MINE_NOT_EXIST.send(sender, args.get(0));
            return;
        }

        execute(plugin, sender, args.subList(1, args.size()), cataMine);
    }

}
