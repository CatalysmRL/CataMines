package me.catalysmrl.catamines.command.abstraction;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.message.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

/**
 * A parent command containing any number of child commands.
 */
public abstract class ParentMineCommand extends AbstractCataCommand {

    /**
     * All sub commands of this command
     */
    private final List<AbstractCataMineCommand> children;

    public ParentMineCommand(String name, List<AbstractCataMineCommand> children) {
        super(name, null, integer -> true, false);
        this.children = children;
    }

    public List<AbstractCataMineCommand> getChildren() {
        return this.children;
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args) throws CommandException {

        if (args.size() < 2) {
            Messages.send(sender, getUsage());
            return;
        }

        AbstractCataMineCommand sub = getChildren().stream()
                .filter(s -> s.getName().equalsIgnoreCase(args.get(1)) || getAliases().contains(s.getName().toLowerCase(Locale.ROOT)))
                .findFirst()
                .orElse(null);

        if (sub == null) {
            Message.UNKNOWN_COMMAND.send(sender);
            return;
        }

        if (sub.onlyPlayers() && !(sender instanceof Player)) {
            Message.ONLY_PLAYERS.send(sender);
            return;
        }

        if (!sub.isAuthorized(sender)) {
            Message.NO_PERMISSION.send(sender);
            return;
        }

        if (!sub.checkArgLength().test(args.size())) {
            Messages.send(sender, getUsage());
            return;
        }

        String mineID = args.get(0);

        Optional<CataMine> mineOptional = plugin.getMineManager().getMine(mineID);

        if (mineOptional.isEmpty()) {
            Message.MINE_NOT_EXIST.send(sender, mineID);
            return;
        }

        sub.execute(plugin, sender, args.subList(1, args.size()), mineOptional.get());
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, List<String> args) {
        if (args.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> availableSubCommands = new ArrayList<>();

        children.stream()
                .filter(c -> c.isAuthorized(sender))
                .forEach(c -> {
                    availableSubCommands.add(c.getName());
                    availableSubCommands.addAll(c.getAliases());
                });

        return StringUtil.copyPartialMatches(args.get(0), availableSubCommands, new ArrayList<>());
    }
}
