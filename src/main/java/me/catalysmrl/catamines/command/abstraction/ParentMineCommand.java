package me.catalysmrl.catamines.command.abstraction;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.mine.abstraction.CataMine;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.message.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * A parent command containing any number of child commands.
 */
public abstract class ParentMineCommand extends AbstractCataCommand {

    /**
     * All sub commands of this command
     */
    private final List<CataCommand> children;

    public ParentMineCommand(String name, List<CataCommand> children) {
        super(name, null, integer -> true, false);
        this.children = children;
    }

    public List<CataCommand> getChildren() {
        return this.children;
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args) throws CommandException {

        if (args.size() < 2) {
            Messages.send(sender, getUsage());
            return;
        }

        CataCommand sub = getChildren().stream()
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

        CataMine mine = plugin.getMineManager().getMine(args.get(0));

        if (mine == null) {
            Message.MINE_NOT_EXIST.send(sender, args.get(0));
            return;
        }

        sub.execute(plugin, sender, args.subList(1, args.size()), mine);
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, List<String> args) {
        if (args.size() == 0) {
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
