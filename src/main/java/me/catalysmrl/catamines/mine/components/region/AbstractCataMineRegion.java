package me.catalysmrl.catamines.mine.components.region;

import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCataMineRegion implements CataMineRegion {

    protected String name;
    private final List<CataMineComposition> compositions;

    public AbstractCataMineRegion(String name) {
        this.name = name;
        compositions = new ArrayList<>();
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
    public List<CataMineComposition> getCompositions() {
        return compositions;
    }

    @Override
    public String toString() {
        return "AbstractCataMineRegion{" +
                "name='" + name + '\'' +
                ", compositions=" + compositions +
                '}';
    }
}
