package me.catalysmrl.catamines.mine.reward.trigger.context;

import org.bukkit.entity.Player;

import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.mine.components.composition.CataMineBlock;
import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.mine.reward.trigger.TriggerContext;

import org.bukkit.Location;

public record BlockBreakContext(
    Player player,
    Location location,
    CataMine mine,
    CataMineRegion region,
    CataMineComposition composition,
    CataMineBlock block
) implements TriggerContext {}
