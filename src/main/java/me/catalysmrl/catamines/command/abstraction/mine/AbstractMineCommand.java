package me.catalysmrl.catamines.command.abstraction.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.AbstractCommand;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.utils.ArgumentException;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.message.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class AbstractMineCommand extends AbstractCommand {

    public AbstractMineCommand(String name, String permission, Predicate<Integer> argumentCheck, boolean onlyPlayers) {
        super(name, permission, argumentCheck, onlyPlayers);
    }

    /**
     * Represents a mine command. The mine is automatically parsed from the arguments and executes
     * method implemented by CataMine interface.
     *
     * @param plugin The CataMines plugin instance
     * @param sender The command sender
     * @param args   The command arguments passed by the sender
     * @throws CommandException If there is an error during command execution
     */
    @Override
    public final void execute(CataMines plugin, CommandSender sender, List<String> args) throws CommandException {

        if (args.isEmpty()) {
            throw new ArgumentException.Usage();
        }

        String mineID = args.get(0);

        if ("*".equals(mineID)) {
            if (!sender.hasPermission("catamines.admin")) {
                Message.NO_PERMISSION.send(sender);
                return;
            }

            List<CataMine> mines = new ArrayList<>(plugin.getMineManager().getMines());
            List<String> strippedArgs = args.subList(1, args.size());

            for (CataMine mine : mines) {
                execute(plugin, sender, strippedArgs, mine);
            }

            sender.sendMessage("");
            Message.QUERY_ALL.send(sender, mines.size());
            sender.sendMessage("");
            return;
        }

        Optional<CataMine> mineOptional = plugin.getMineManager().getMine(mineID);

        if (mineOptional.isEmpty()) {
            Message.MINE_NOT_EXISTS.send(sender, mineID);
            return;
        }

        execute(plugin, sender, args.subList(1, args.size()), mineOptional.get());
    }

    public abstract void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine);

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, List<String> args) {
        if (args.size() == 1) {
            return StringUtil.copyPartialMatches(args.get(0), plugin.getMineManager().getMineList(), new ArrayList<>());
        } else {
            Optional<CataMine> optionalCataMine = plugin.getMineManager().getMine(args.get(0));
            if (optionalCataMine.isEmpty()) return List.of(Messages.colorize("&cUnknown mine"));
            CataMine mine = optionalCataMine.get();

            return tabComplete(plugin, sender, args.subList(1, args.size()), mine);
        }
    }

    public List<String> tabComplete(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) {
        return Collections.emptyList();
    }
}
