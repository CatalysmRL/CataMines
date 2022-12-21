package me.catalysmrl.catamines.mine.abstraction;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.mine.abstraction.region.CataMineRegion;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractCataMine implements CataMine {

    protected String name;
    protected String displayName;
    protected List<CataMineRegion> regions;

    public AbstractCataMine(String name) {
        this.name = name;
        this.displayName = name;
    }

    @Override
    public void tick() {

    }

    @Override
    public void reset() {
        CataMines.getInstance().getMineManager().resetRegion(getRegion());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public List<CataMineRegion> getRegions() {
        return regions;
    }

    public CataMineRegion getRegion() {
        double rand = regions.stream()
                .mapToDouble(CataMineRegion::getChance)
                .sum() * ThreadLocalRandom.current().nextDouble();

        CataMineRegion choice = null;
        for (CataMineRegion region : regions) {
            choice = region;
            rand -= choice.getChance();
            if (rand < 0) break;
        }

        return choice;
    }
}
