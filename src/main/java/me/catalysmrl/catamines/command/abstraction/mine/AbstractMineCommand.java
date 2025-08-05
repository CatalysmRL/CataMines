package me.catalysmrl.catamines.command.abstraction.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.AbstractCommand;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.utils.ArgumentException;
import me.catalysmrl.catamines.utils.message.LegacyMessage;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.message.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class AbstractMineCommand extends AbstractCommand {

    private boolean needsSave = false;

    public AbstractMineCommand(String name, String permission, Predicate<Integer> argumentCheck, boolean onlyPlayers) {
        super(name, permission, argumentCheck, onlyPlayers);
    }

    /**
     * Represents a mine command. The mine is automatically parsed from the arguments and executes
     * method implemented by CataMine interface.
     *
     * @param plugin The CataMines plugin instance
     * @param sender The command sender
     * @param ctx    The command context
     * @throws CommandException If there is an error during command execution
     */
    @Override
    public final void execute(CataMines plugin, CommandSender sender, CommandContext ctx) throws CommandException {
        String mineID = ctx.next();
        if (mineID == null) throw new ArgumentException.Usage();

        if ("*".equals(mineID)) {
            if (!sender.hasPermission("catamines.admin")) {
                Message.NO_PERMISSION.send(sender);
                return;
            }

            List<CataMine> mines = new ArrayList<>(plugin.getMineManager().getMines());

            for (CataMine mine : mines) {
                Messages.sendPrefixed(sender, "&7>> &f" + mine.getName());
                execute(plugin, sender, ctx.copy(), mine);
            }

            sender.sendMessage("");
            LegacyMessage.QUERY_ALL.send(sender, mines.size());
            sender.sendMessage("");
            return;
        }

        Optional<CataMine> mineOptional = plugin.getMineManager().getMine(mineID);

        if (mineOptional.isEmpty()) {
            LegacyMessage.MINE_NOT_EXISTS.send(sender, mineID);
            return;
        }

        CataMine mine = mineOptional.get();

        execute(plugin, sender, ctx, mine);

        if (needsSave) {
            try {
                plugin.getMineManager().saveMine(mine);
            } catch (IOException e) {
                LegacyMessage.MINE_SAVE_EXCEPTION.send(sender);
            }
        }
    }

    public abstract void execute(CataMines plugin, CommandSender sender, CommandContext ctx, CataMine mine) throws CommandException;

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx) {
        if (ctx.remaining() == 1) {
            return StringUtil.copyPartialMatches(ctx.peek(), plugin.getMineManager().getMineList(), new ArrayList<>());
        }

        String mineId = ctx.peek();
        if ("*".equals(mineId)) {
            Optional<CataMine> mineOptional = plugin.getMineManager().getMines().stream().findAny();
            if (mineOptional.isEmpty())
                return Collections.singletonList(Message.of("command.mine-not-exists").format(sender));

            ctx.next();

            return tabComplete(plugin, sender, ctx.copy(), mineOptional.get());
        }

        Optional<CataMine> optionalCataMine = plugin.getMineManager().getMine(mineId);
        if (optionalCataMine.isEmpty()) {
            return Collections.singletonList(Message.of("command.mine-not-exists").format(sender));
        }

        ctx.next();

        return tabComplete(plugin, sender, ctx, optionalCataMine.get());
    }

    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext context, CataMine mine) {
        return Collections.emptyList();
    }

    protected void requireSave() {
        this.needsSave = true;
    }
}
