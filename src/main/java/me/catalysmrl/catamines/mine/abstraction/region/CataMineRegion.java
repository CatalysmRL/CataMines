package me.catalysmrl.catamines.mine.abstraction.region;

import me.catalysmrl.catamines.mine.abstraction.reward.Rewardable;
import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.List;

public interface CataMineRegion extends Rewardable, ConfigurationSerializable {

    void fill();

    RegionType getType();

    List<CataMineComposition> getCompositions();

    enum RegionType {
        SELECTION,
        SCHEMATIC
    }
}
