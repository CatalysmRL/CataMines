package me.catalysmrl.catamines.mine.abstraction.region;

import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import me.catalysmrl.catamines.mine.abstraction.reward.CataMineReward;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCataMineRegion implements CataMineRegion {

    private double chance;
    private CataMineReward reward;
    private final List<CataMineComposition> compositions = new ArrayList<>();

    @Override
    public double getChance() {
        return chance;
    }

    @Override
    public void setChance(double chance) {
        this.chance = chance;
    }

    @Override
    public CataMineReward getReward() {
        return reward;
    }

    @Override
    public void setReward(CataMineReward reward) {
        this.reward = reward;
    }

    @Override
    public List<CataMineComposition> getCompositions() {
        return compositions;
    }
}
