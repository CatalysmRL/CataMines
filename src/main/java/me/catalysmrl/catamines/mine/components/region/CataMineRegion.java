package me.catalysmrl.catamines.mine.components.region;

import com.sk89q.worldedit.regions.RegionSelector;
import me.catalysmrl.catamines.api.serialization.SectionSerializable;
import me.catalysmrl.catamines.api.mine.PropertyHolder;
import me.catalysmrl.catamines.api.mine.Targetable;
import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import me.catalysmrl.catamines.mine.components.manager.choice.Choice;
import me.catalysmrl.catamines.mine.components.manager.choice.ChoiceManager;
import me.catalysmrl.catamines.mine.components.manager.choice.Identifiable;
import me.catalysmrl.catamines.mine.reward.Rewardable;

public interface CataMineRegion
        extends Rewardable, Identifiable, Choice, SectionSerializable, PropertyHolder, Targetable, Cloneable {

    String getName();

    void setName(String name);

    void fill();

    void redefineRegion(RegionSelector selector);

    RegionType getType();

    ChoiceManager<CataMineComposition> getCompositionManager();

    long getVolume();

    enum RegionType {
        SELECTION,
        SCHEMATIC
    }
}
