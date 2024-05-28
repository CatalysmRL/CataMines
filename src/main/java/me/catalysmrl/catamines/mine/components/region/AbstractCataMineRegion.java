package me.catalysmrl.catamines.mine.components.region;

import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import me.catalysmrl.catamines.mine.components.manager.choice.ChoiceManager;

public abstract class AbstractCataMineRegion implements CataMineRegion {

    protected String name;
    protected double chance;
    protected ChoiceManager<CataMineComposition> compositionManager;

    public AbstractCataMineRegion(String name) {
        this.name = name;
        this.compositionManager = new ChoiceManager<>();
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
    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    @Override
    public ChoiceManager<CataMineComposition> getCompositionManager() {
        return compositionManager;
    }

    @Override
    public String toString() {
        return "AbstractCataMineRegion{" +
                "name='" + name + '\'' +
                ", chance=" + chance +
                ", compositionManager=" + compositionManager +
                '}';
    }
}
