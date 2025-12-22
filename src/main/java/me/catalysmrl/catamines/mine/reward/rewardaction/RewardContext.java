package me.catalysmrl.catamines.mine.reward.rewardaction;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;

public class RewardContext {

    private final Player player;
    private final Location location;
    private final CataMine mine;
    private final CataMineRegion region;
    private final CataMineComposition composition;

    public RewardContext(Player player, Location location, CataMine mine, CataMineRegion region,
            CataMineComposition composition) {
        this.player = player;
        this.location = location;
        this.mine = mine;
        this.region = region;
        this.composition = composition;
    }

    public Player getPlayer() { return player; }
    public Location getLocation() { return location; }
    public CataMine getMine() { return mine; }
    public CataMineRegion getRegion() { return region; }
    public CataMineComposition getComposition() { return composition; }
}
