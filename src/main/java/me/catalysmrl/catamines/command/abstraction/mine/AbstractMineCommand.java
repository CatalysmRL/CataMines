package me.catalysmrl.catamines.command.abstraction.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.AbstractCommand;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.command.utils.ArgumentException;
import me.catalysmrl.catamines.command.utils.MineTarget;
import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
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

/**
 * Represents a mine command. The mine is automatically parsed from the
 * arguments and executes
 * method implemented by CataMine interface.
 *
 * @param plugin The CataMines plugin instance
 * @param sender The command sender
 * @param ctx    The command context
 * @throws CommandException If there is an error during command execution
 */
public abstract class AbstractMineCommand extends AbstractCommand {

    private boolean needsSave = false;

    public AbstractMineCommand(String name, String permission, Predicate<Integer> argumentCheck, boolean onlyPlayers) {
        super(name, permission, argumentCheck, onlyPlayers);
    }

    @Override
    public final void execute(CataMines plugin, CommandSender sender, CommandContext ctx) throws CommandException {
        String arg = ctx.next();
        if (arg == null)
            throw new ArgumentException.Usage();

        if ("*".equals(arg)) {
            if (!sender.hasPermission("catamines.admin")) {
                Message.NO_PERMISSION.send(sender);
                return;
            }

            List<CataMine> mines = new ArrayList<>(plugin.getMineManager().getMines());

            for (CataMine mine : mines) {
                Messages.sendPrefixed(sender, "&7>> &f" + mine.getName());
                execute(plugin, sender, ctx.copy(), new MineTarget(mine, null, null));
            }

            sender.sendMessage("");
            Message.QUERY_ALL.send(sender, mines.size());
            sender.sendMessage("");
            return;
        }

        String[] parts = arg.split(":", 2);
        String mineID = parts[0];
        String path = parts.length > 1 ? parts[1] : null;

        Optional<CataMine> mineOptional = plugin.getMineManager().getMine(mineID);

        if (mineOptional.isEmpty()) {
            Message.MINE_NOT_EXISTS.send(sender, mineID);
            return;
        }

        CataMine mine = mineOptional.get();

        boolean executedWildcard = false;
        if ("*".equals(path)) {
            for (CataMineRegion region : mine.getRegionManager().getChoices()) {
                execute(plugin, sender, ctx.copy(), new MineTarget(mine, region, null));
            }
            executedWildcard = true;
        } else if (path != null && path.endsWith(":*")) {
            String regionName = path.substring(0, path.length() - 2);
            Optional<CataMineRegion> regionOpt = mine.getRegionManager().get(regionName);
            if (regionOpt.isPresent()) {
                CataMineRegion region = regionOpt.get();
                for (CataMineComposition composition : region.getCompositionManager().getChoices()) {
                    execute(plugin, sender, ctx.copy(), new MineTarget(mine, region, composition));
                }
                executedWildcard = true;
            }
        }

        if (!executedWildcard) {
            MineTarget target = MineTarget.resolve(mine, path);
            execute(plugin, sender, ctx, target);
        }

        if (needsSave) {
            try {
                plugin.getMineManager().saveMine(mine);
            } catch (IOException e) {
                Message.MINE_SAVE_EXCEPTION.send(sender);
            }
        }
    }

    public abstract void execute(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target)
            throws CommandException;

    @Override
    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx) {
        if (ctx.remaining() == 1) {
            String arg = ctx.peek();

            List<String> authorizedMines = plugin.getMineManager().getMineList().stream()
                    .filter(mineName -> {
                        String perm = getPermission().orElse(null);
                        return perm == null || sender.hasPermission(perm + "." + mineName);
                    })
                    .toList();

            if (!arg.contains(":")) {
                return StringUtil.copyPartialMatches(arg, authorizedMines, new ArrayList<>());
            }

            String[] parts = arg.split(":", -1);
            String mineName = parts[0];

            if (!authorizedMines.contains(mineName)) {
                return Collections.emptyList();
            }

            Optional<CataMine> mineOpt = plugin.getMineManager().getMine(mineName);

            if (mineOpt.isEmpty()) {
                return Collections.emptyList();
            }

            CataMine mine = mineOpt.get();

            if (parts.length == 2) {
                List<String> candidates = new ArrayList<>();
                for (CataMineRegion region : mine.getRegionManager().getChoices()) {
                    candidates.add(mineName + ":" + region.getName());
                }
                return StringUtil.copyPartialMatches(arg, candidates, new ArrayList<>());
            }

            if (parts.length == 3) {
                String regionName = parts[1];
                Optional<CataMineRegion> regionOpt = mine.getRegionManager().get(regionName);

                if (regionOpt.isEmpty()) {
                    return Collections.emptyList();
                }

                List<String> candidates = new ArrayList<>();
                for (CataMineComposition comp : regionOpt.get().getCompositionManager().getChoices()) {
                    candidates.add(mineName + ":" + regionName + ":" + comp.getName());
                }
                return StringUtil.copyPartialMatches(arg, candidates, new ArrayList<>());
            }

            return Collections.emptyList();
        }

        String mineId = ctx.peek();
        if ("*".equals(mineId)) {
            Optional<CataMine> mineOptional = plugin.getMineManager().getMines().stream().findAny();
            if (mineOptional.isEmpty())
                return Collections.singletonList(Message.MINE_NOT_EXISTS.format(sender, mineId));

            ctx.next();

            return tabComplete(plugin, sender, ctx.copy(), mineOptional.get());
        }

        Optional<CataMine> optionalCataMine = plugin.getMineManager().getMine(mineId);
        if (optionalCataMine.isEmpty()) {
            return Collections.singletonList(Message.MINE_NOT_EXISTS.format(sender, mineId));
        }

        // Check permission for the specific mine if it's not a wildcard
        String perm = getPermission().orElse(null);
        if (perm != null && !sender.hasPermission(perm + "." + mineId)) {
            return Collections.emptyList();
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
