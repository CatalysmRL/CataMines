package me.catalysmrl.catamines.command.targetutil;

import java.util.Optional;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;

public final class TargetParser {
    private TargetParser() {
    }

    public static Optional<CommandTarget> parse(CataMines plugin, String input) {
        if (input == null || input.isBlank())
            return Optional.empty();
        String t = input.trim();

        return switch (t) {
            case "*" -> Optional.of(new CommandTarget.AllMines());
            case "*:*" -> Optional.of(new CommandTarget.AllRegions());
            case "*:*:*" -> Optional.of(new CommandTarget.AllCompositions());
            default -> {
                String[] p = t.split(":", 3);
                var mineOpt = plugin.getMineManager().getMine(p[0]);
                if (mineOpt.isEmpty())
                    yield Optional.empty();

                CataMine mine = mineOpt.get();

                if (p.length == 1)
                    yield Optional.of(new CommandTarget.Mine(mine));
                if (p.length == 2 && "*".equals(p[1]))
                    yield Optional.of(new CommandTarget.MineAllRegions(mine));
                if (p.length == 2) {
                    var region = mine.getRegionManager().get(p[1]);
                    yield region.map(r -> new CommandTarget.Region(mine, r));
                }
                if (p.length == 3 && "*".equals(p[2])) {
                    var region = mine.getRegionManager().get(p[1]);
                    if (region.isEmpty())
                        yield Optional.empty();
                    yield Optional.of(new CommandTarget.RegionAllCompositions(mine, region.get()));
                }
                if (p.length == 3) {
                    var region = mine.getRegionManager().get(p[1]);
                    if (region.isEmpty())
                        yield Optional.empty();
                    var comp = region.get().getCompositionManager().get(p[2]);
                    yield comp.map(c -> new CommandTarget.Composition(mine, region.get(), c));
                }
                yield Optional.empty();
            }
        };
    }
}
