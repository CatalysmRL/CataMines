package me.catalysmrl.catamines.command.abstraction.mine;

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
public abstract class ParentMineCommand extends AbstractMineCommand {

    /**
     * All sub commands of this command
     */
    private final List<AbstractMineCommand> children;

    public ParentMineCommand(String name, List<AbstractMineCommand> children) {
        super(name, null, integer -> true, false);
        this.children = children;
    }

    public List<AbstractMineCommand> getChildren() {
        return this.children;
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) {

        if (args.isEmpty()) {
            Messages.send(sender, getUsage());
            return;
        }

        AbstractMineCommand sub = getChildren().stream()
                .filter(s -> s.getName().equalsIgnoreCase(args.get(0)) || s.getAliases().contains(args.get(0).toLowerCase(Locale.ROOT)))
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
            Messages.send(sender, sub.getUsage());
            return;
        }

        sub.execute(plugin, sender, args.subList(1, args.size()), mine);
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) {
        if (args.size() == 1) {
            List<String> availableSubCommands = new ArrayList<>();

            children.stream()
                    .filter(c -> c.isAuthorized(sender))
                    .forEach(c -> {
                        availableSubCommands.add(c.getName());
                        availableSubCommands.addAll(c.getAliases());
                    });

            return StringUtil.copyPartialMatches(args.get(0), availableSubCommands, new ArrayList<>());
        } else {
            Optional<AbstractMineCommand> subCommand = children.stream()
                    .filter(c -> c.isAuthorized(sender))
                    .filter(c -> c.getName().equalsIgnoreCase(args.get(0)) || c.getAliases().contains(args.get(0).toLowerCase(Locale.ROOT)))
                    .findAny();

            if (subCommand.isEmpty()) {
                return Collections.emptyList();
            }

            return subCommand.get().tabComplete(plugin, sender, args.subList(1, args.size()), mine);
        }
    }
}
