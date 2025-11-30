package me.catalysmrl.catamines.command.abstraction.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.AbstractCommand;
import me.catalysmrl.catamines.command.utils.ArgumentException;
import me.catalysmrl.catamines.command.utils.CommandContext;
import me.catalysmrl.catamines.command.utils.CommandException;
import me.catalysmrl.catamines.command.utils.MineTarget;
import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class AbstractMineCommand extends AbstractCommand {

    private boolean needsSave = false;

    public AbstractMineCommand(String name, String permission, java.util.function.Predicate<Integer> argumentCheck,
            boolean onlyPlayers) {
        super(name, permission, argumentCheck, onlyPlayers);
    }

    @Override
    public final void execute(CataMines plugin, CommandSender sender, CommandContext ctx) throws CommandException {
        String targetPath = ctx.next();
        if (targetPath == null)
            throw new ArgumentException.Usage();

        // === GLOBAL WILDCARDS ===
        if (targetPath.equals("*")) {
            if (!sender.hasPermission("catamines.admin")) {
                Message.NO_PERMISSION.send(sender);
                return;
            }

            List<CataMine> mines = new ArrayList<>(plugin.getMineManager().getMines());
            if (mines.isEmpty()) {
                Message.NO_MINES.send(sender);
                return;
            }

            Message.QUERY_ALL_HEADER.sendList(sender);
            for (CataMine mine : mines) {
                Message.QUERY_ALL_ENTRY.send(sender, mine.getName());
                execute(plugin, sender, ctx.copy(), new MineTarget(mine, null, null));
            }
            Message.QUERY_ALL_FOOTER.sendList(sender, mines.size());
            return;
        }

        // === *:* → All mines, all regions (default composition) ===
        else if (targetPath.equals("*:*")) {
            if (!sender.hasPermission("catamines.admin")) {
                Message.NO_PERMISSION.send(sender);
                return;
            }

            List<CataMine> mines = new ArrayList<>(plugin.getMineManager().getMines());
            if (mines.isEmpty()) {
                Message.NO_MINES.send(sender);
                return;
            }

            int total = 0;
            Message.QUERY_ALL_HEADER.sendList(sender);

            for (CataMine mine : mines) {
                for (CataMineRegion region : mine.getRegionManager().getChoices()) {
                    total++;
                    execute(plugin, sender, ctx.copy(), new MineTarget(mine, region, null));
                }
            }

            Message.QUERY_ALL_FOOTER.sendList(sender, total);
            return;
        }

        // === *:*:* → All mines, all regions, all compositions ===
        else if (targetPath.equals("*:*:*")) {
            if (!sender.hasPermission("catamines.admin")) {
                Message.NO_PERMISSION.send(sender);
                return;
            }

            List<CataMine> mines = new ArrayList<>(plugin.getMineManager().getMines());
            if (mines.isEmpty()) {
                Message.NO_MINES.send(sender);
                return;
            }

            int total = 0;
            Message.QUERY_ALL_HEADER.sendList(sender);

            for (CataMine mine : mines) {
                for (CataMineRegion region : mine.getRegionManager().getChoices()) {
                    for (CataMineComposition comp : region.getCompositionManager().getChoices()) {
                        total++;
                        execute(plugin, sender, ctx.copy(), new MineTarget(mine, region, comp));
                    }
                }
            }

            Message.QUERY_ALL_FOOTER.sendList(sender, total);
            return;
        }

        else {

            // === NORMAL PATH (specific mine, optional region/comp with wildcards) ===
            String[] parts = targetPath.split(":", 3);
            String mineName = parts[0];
            String regionPart = parts.length > 1 ? parts[1] : null;
            String compPart = parts.length > 2 ? parts[2] : null;

            Optional<CataMine> mineOpt = plugin.getMineManager().getMine(mineName);
            if (mineOpt.isEmpty()) {
                Message.MINE_NOT_EXISTS.send(sender, mineName);
                return;
            }
            CataMine mine = mineOpt.get();

            boolean executed = false;

            // mine:*
            if ("*".equals(regionPart)) {
                for (CataMineRegion region : mine.getRegionManager().getChoices()) {
                    execute(plugin, sender, ctx.copy(), new MineTarget(mine, region, null));
                }
                executed = true;
            }
            // mine:region:*
            else if (regionPart != null && "*".equals(compPart)) {
                Optional<CataMineRegion> regionOpt = mine.getRegionManager().get(regionPart);
                if (regionOpt.isPresent()) {
                    for (CataMineComposition comp : regionOpt.get().getCompositionManager().getChoices()) {
                        execute(plugin, sender, ctx.copy(), new MineTarget(mine, regionOpt.get(), comp));
                    }
                    executed = true;
                } else {
                    Message.REGIONS_NOT_EXISTS.send(sender, regionPart, mine.getName());
                    return;
                }
            }
            // Normal single target
            else {
                String path = regionPart == null ? null : regionPart + (compPart == null ? "" : ":" + compPart);
                MineTarget target = MineTarget.resolve(mine, path);
                execute(plugin, sender, ctx, target);
                executed = true;
            }

            if (!executed) {
                Message.INVALID_TARGET.send(sender, targetPath);
                return;
            }

            if (needsSave) {
                try {
                    plugin.getMineManager().saveMine(mine);
                    needsSave = false;
                } catch (IOException e) {
                    Message.MINE_SAVE_EXCEPTION.send(sender);
                    e.printStackTrace();
                }
            }
        }
    }

    public abstract void execute(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target)
            throws CommandException;

    @Override
    public final List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx) {
        // Case 1: Still completing the first argument (the target path)
        if (ctx.remaining() > 1) {
            String firstArg = ctx.args().get(0);

            // Global wildcard (admin only)
            if ("*".equals(firstArg) && sender.hasPermission("catamines.admin")) {
                return Collections.emptyList();
            }

            MineTarget target = resolveTargetSafe(plugin, firstArg);
            if (target == null) {
                return Collections.emptyList(); // invalid path → no suggestions
            }

            ctx.next(); // consume the target
            return tabComplete(plugin, sender, ctx, target);
        }

        // Case 2: Completing the target path itself
        String input = ctx.peek() != null ? ctx.peek() : "";
        return completeTargetPath(plugin, sender, input);
    }

    /**
     * Safely resolves a path string → MineTarget.
     * Returns null if path contains wildcards or is invalid.
     */
    private MineTarget resolveTargetSafe(CataMines plugin, String input) {
        if ("*".equals(input))
            return null;

        String[] parts = input.split(":", 3);
        String mineName = parts[0];

        Optional<CataMine> mineOpt = plugin.getMineManager().getMine(mineName);
        if (mineOpt.isEmpty())
            return null;

        CataMine mine = mineOpt.get();

        // Build path part safely
        String path = parts.length > 1 ? parts[1] + (parts.length > 2 ? ":" + parts[2] : "") : null;

        // If path is null → mine only → valid
        // If path contains wildcard → we don't resolve it here
        if (path != null && (path.equals("*") || path.endsWith(":*"))) {
            return null;
        }

        // Empty path after mine: → also valid (means mine only)
        if (path != null && path.isEmpty()) {
            path = null;
        }

        return MineTarget.resolve(mine, path);
    }

    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx,
            MineTarget target) {
        return Collections.emptyList();
    }

    private List<String> completeTargetPath(CataMines plugin, CommandSender sender, String input) {
        List<String> completions = new ArrayList<>();

        // Global wildcard for admins
        if (sender.hasPermission("catamines.admin") && "*".startsWith(input.toLowerCase())) {
            completions.add("*");
        }

        List<String> authorizedMines = plugin.getMineManager().getMineList().stream()
                .filter(mineName -> {
                    String perm = getPermission().orElse(null);
                    return perm == null || sender.hasPermission(perm + "." + mineName);
                })
                .toList();

        // Completing mine name
        if (!input.contains(":")) {
            StringUtil.copyPartialMatches(input, authorizedMines, completions);
            return completions;
        }

        String[] parts = input.split(":", -1);
        String mineName = parts[0];

        if (!authorizedMines.contains(mineName)) {
            return completions;
        }

        Optional<CataMine> mineOpt = plugin.getMineManager().getMine(mineName);
        if (mineOpt.isEmpty())
            return completions;
        CataMine mine = mineOpt.get();

        // Completing region: mine:<partial>
        if (parts.length == 2) {
            String regionInput = parts[1];
            if ("*".startsWith(regionInput)) {
                completions.add(mineName + ":*");
            }
            for (CataMineRegion r : mine.getRegionManager().getChoices()) {
                String cand = mineName + ":" + r.getName();
                if (cand.startsWith(input)) {
                    completions.add(cand);
                }
            }
        }
        // Completing composition: mine:region:<partial>
        else if (parts.length == 3) {
            String regionName = parts[1];
            String compInput = parts[2];

            if ("*".startsWith(compInput)) {
                completions.add(mineName + ":" + regionName + ":*");
            }

            mine.getRegionManager().get(regionName).ifPresent(region -> {
                for (CataMineComposition c : region.getCompositionManager().getChoices()) {
                    String cand = mineName + ":" + regionName + ":" + c.getName();
                    if (cand.startsWith(input)) {
                        completions.add(cand);
                    }
                }
            });
        }

        return completions;
    }

    protected void requireSave() {
        this.needsSave = true;
    }
}