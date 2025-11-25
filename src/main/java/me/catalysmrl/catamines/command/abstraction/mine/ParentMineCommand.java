package me.catalysmrl.catamines.command.abstraction.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.utils.ArgumentException;
import me.catalysmrl.catamines.utils.message.Message;
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
        super(name, "catamines.regions", integer -> true, false);
        this.children = children;
    }

    public List<AbstractMineCommand> getChildren() {
        return this.children;
    }

    @Override
    public void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine) throws CommandException {

        if (!ctx.hasNext()) throw new ArgumentException.Usage();

        String subCmdName = ctx.peek();

        AbstractMineCommand sub = getChildren().stream()
                .filter(s -> s.getName().equalsIgnoreCase(subCmdName) || s.getAliases().contains(subCmdName.toLowerCase(Locale.ROOT)))
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

        ctx.next();

        sub.execute(plugin, sender, ctx, mine);
    }

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine) {
        if (ctx.remaining() == 1) {
            List<String> availableSubCommands = new ArrayList<>();

            children.stream()
                    .filter(c -> c.isAuthorized(sender))
                    .forEach(c -> {
                        availableSubCommands.add(c.getName());
                        availableSubCommands.addAll(c.getAliases());
                    });

            return StringUtil.copyPartialMatches(ctx.peek(), availableSubCommands, new ArrayList<>());
        } else {
            Optional<AbstractMineCommand> subCommand = children.stream()
                    .filter(c -> c.isAuthorized(sender))
                    .filter(c -> c.getName().equalsIgnoreCase(ctx.peek()) || c.getAliases().contains(ctx.peek().toLowerCase(Locale.ROOT)))
                    .findAny();

            if (subCommand.isEmpty()) {
                return Collections.emptyList();
            }

            ctx.next();

            return subCommand.get().tabComplete(plugin, sender, ctx, mine);
        }
    }
}
