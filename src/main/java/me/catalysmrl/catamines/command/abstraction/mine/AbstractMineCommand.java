package me.catalysmrl.catamines.command.abstraction.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.command.abstraction.AbstractCommand;
import me.catalysmrl.catamines.command.targetutil.CommandTarget;
import me.catalysmrl.catamines.command.targetutil.TargetParser;
import me.catalysmrl.catamines.command.utils.ArgumentException;
import me.catalysmrl.catamines.command.utils.CommandContext;
import me.catalysmrl.catamines.command.utils.CommandException;
import me.catalysmrl.catamines.command.utils.MineTarget;
import me.catalysmrl.catamines.managers.ConfirmationManager;
import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.undo.UndoManager;

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
        String rawTarget = ctx.next();
        if (rawTarget == null)
            throw new ArgumentException.Usage();

        var targetOpt = TargetParser.parse(plugin, rawTarget);
        if (targetOpt.isEmpty()) {
            Message.INVALID_TARGET.send(sender, rawTarget);
            return;
        }

        CommandTarget target = targetOpt.get();

        // Use Message.
        if (target.isGlobal()) {
            ConfirmationManager.request(sender,
                    estimateAffected(target, plugin),
                    () -> processCommand(plugin, sender, ctx, target, rawTarget));
            return;
        }

        processCommand(plugin, sender, ctx, target, rawTarget);
    }

    public abstract void execute(CataMines plugin, CommandSender sender, CommandContext ctx, MineTarget target)
            throws CommandException;

    @Override
    public final List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx) {
        if (ctx.remaining() > 1) {
            String firstArg = ctx.args().get(0);

            if ("*".equals(firstArg) && sender.hasPermission("catamines.admin")) {
                return Collections.emptyList();
            }

            MineTarget target = resolveTargetSafe(plugin, firstArg);
            if (target == null) {
                return Collections.emptyList();
            }

            ctx.next();
            return tabComplete(plugin, sender, ctx, target);
        }

        String input = ctx.peek() != null ? ctx.peek() : "";
        return completeTargetPath(plugin, sender, input);
    }

    public List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext ctx,
            MineTarget target) {
        return Collections.emptyList();
    }

    private void processCommand(CataMines plugin, CommandSender sender, CommandContext ctx, CommandTarget target,
            String rawTarget) {
        List<CataMine> affected = collectAffectedMines(target, plugin);
        if (!affected.isEmpty()) {
            UndoManager.record(sender, affected, getName() + " " + rawTarget);
        }

        if (target instanceof CommandTarget.AllMines) {
            new ArrayList<>(plugin.getMineManager().getMines())
                    .forEach(mine -> executeSafe(plugin, sender, ctx.copy(), mine, null, null));

        } else if (target instanceof CommandTarget.AllRegions) {
            new ArrayList<>(plugin.getMineManager().getMines()).forEach(mine -> new ArrayList<>(mine.getRegionManager().getChoices())
                    .forEach(region -> executeSafe(plugin, sender, ctx.copy(), mine, region, null)));

        } else if (target instanceof CommandTarget.AllCompositions) {
            new ArrayList<>(plugin.getMineManager().getMines())
                    .forEach(mine -> new ArrayList<>(mine.getRegionManager().getChoices())
                            .forEach(region -> new ArrayList<>(region.getCompositionManager().getChoices())
                                    .forEach(comp -> executeSafe(plugin, sender, ctx.copy(), mine, region, comp))));

        } else if (target instanceof CommandTarget.Mine m) {
            executeSafe(plugin, sender, ctx, m.mine(), null, null);

        } else if (target instanceof CommandTarget.MineAllRegions m) {
            new ArrayList<>(m.mine().getRegionManager().getChoices())
                    .forEach(r -> executeSafe(plugin, sender, ctx.copy(), m.mine(), r, null));

        } else if (target instanceof CommandTarget.Region r) {
            executeSafe(plugin, sender, ctx, r.mine(), r.region(), null);

        } else if (target instanceof CommandTarget.RegionAllCompositions r) {
            new ArrayList<>(r.region().getCompositionManager().getChoices())
                    .forEach(c -> executeSafe(plugin, sender, ctx.copy(), r.mine(), r.region(), c));

        } else if (target instanceof CommandTarget.Composition c) {
            executeSafe(plugin, sender, ctx, c.mine(), c.region(), c.composition());
        }

        if (needsSave) {
            plugin.getMineManager().getMines().forEach(mine -> {
                try {
                    plugin.getMineManager().saveMine(mine);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            needsSave = false;
        }
    }

    private void executeSafe(CataMines plugin, CommandSender sender, CommandContext ctx,
            CataMine mine, CataMineRegion region, CataMineComposition composition) {
        try {
            MineTarget target = new MineTarget(mine, region, composition);
            execute(plugin, sender, ctx, target);
        } catch (CommandException e) {
            // Ignore
        } catch (Exception e) {
            // Ignore
        }
    }

    private int estimateAffected(CommandTarget t, CataMines plugin) {
        return switch (t) {
            case CommandTarget.AllMines ignored -> plugin.getMineManager().getMines().size();
            case CommandTarget.AllRegions ignored -> plugin.getMineManager().getMines().stream()
                    .mapToInt(m -> m.getRegionManager().getChoices().size()).sum();
            case CommandTarget.AllCompositions ignored -> plugin.getMineManager().getMines().stream()
                    .flatMap(m -> m.getRegionManager().getChoices().stream())
                    .mapToInt(r -> r.getCompositionManager().getChoices().size()).sum();
            case CommandTarget.MineAllRegions m -> m.mine().getRegionManager().getChoices().size();
            case CommandTarget.RegionAllCompositions r -> r.region().getCompositionManager().getChoices().size();
            default -> 1;
        };
    }

    private List<CataMine> collectAffectedMines(CommandTarget t, CataMines plugin) {
        List<CataMine> list = new ArrayList<>();

        if (t instanceof CommandTarget.AllMines ||
                t instanceof CommandTarget.AllRegions ||
                t instanceof CommandTarget.AllCompositions) {
            list.addAll(plugin.getMineManager().getMines());
        } else if (t instanceof CommandTarget.Mine mine) {
            list.add(mine.mine());
        } else if (t instanceof CommandTarget.MineAllRegions mineAll) {
            list.add(mineAll.mine());
        } else if (t instanceof CommandTarget.Region region) {
            list.add(region.mine());
        } else if (t instanceof CommandTarget.RegionAllCompositions regionAll) {
            list.add(regionAll.mine());
        } else if (t instanceof CommandTarget.Composition composition) {
            list.add(composition.mine());
        }

        return list;
    }

    private MineTarget resolveTargetSafe(CataMines plugin, String input) {
        if ("*".equals(input))
            return null;

        String[] parts = input.split(":", 3);
        String mineName = parts[0];

        Optional<CataMine> mineOpt = plugin.getMineManager().getMine(mineName);
        if (mineOpt.isEmpty())
            return null;

        CataMine mine = mineOpt.get();

        String path = parts.length > 1 ? parts[1] + (parts.length > 2 ? ":" + parts[2] : "") : null;

        if (path != null && (path.equals("*") || path.endsWith(":*"))) {
            return null;
        }

        if (path != null && path.isEmpty()) {
            path = null;
        }

        return MineTarget.resolve(mine, path);
    }

    private List<String> completeTargetPath(CataMines plugin, CommandSender sender, String input) {
        List<String> completions = new ArrayList<>();

        if (sender.hasPermission("catamines.admin") && "*".startsWith(input.toLowerCase())) {
            completions.add("*");
        }

        List<String> authorizedMines = plugin.getMineManager().getMineList().stream()
                .filter(mineName -> {
                    String perm = getPermission().orElse(null);
                    return perm == null || sender.hasPermission(perm + "." + mineName);
                })
                .toList();

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
        } else if (parts.length == 3) {
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