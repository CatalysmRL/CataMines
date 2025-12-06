package me.catalysmrl.catamines.command.targetutil;

import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;

public sealed interface CommandTarget permits
        CommandTarget.AllMines,
        CommandTarget.AllRegions,
        CommandTarget.AllCompositions,
        CommandTarget.Mine,
        CommandTarget.MineAllRegions,
        CommandTarget.Region,
        CommandTarget.RegionAllCompositions,
        CommandTarget.Composition {

    record AllMines() implements CommandTarget {}
    record AllRegions() implements CommandTarget {}
    record AllCompositions() implements CommandTarget {}

    record Mine(CataMine mine) implements CommandTarget {}
    record MineAllRegions(CataMine mine) implements CommandTarget {}
    record Region(CataMine mine, CataMineRegion region) implements CommandTarget {}
    record RegionAllCompositions(CataMine mine, CataMineRegion region) implements CommandTarget {}
    record Composition(CataMine mine, CataMineRegion region, CataMineComposition composition) implements CommandTarget {}

    default boolean isGlobal() {
        return this instanceof AllMines || this instanceof AllRegions || this instanceof AllCompositions;
    }
}