package me.catalysmrl.catamines.mine.components.region;

import me.catalysmrl.catamines.mine.components.reward.Rewardable;
import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.List;

public interface CataMineRegion extends Rewardable, ConfigurationSerializable {

    String getName();

    void setName(String name);

    void fill();

    RegionType getType();

    List<CataMineComposition> getCompositions();

    long getVolume();

    enum RegionType {
        SELECTION,
        SCHEMATIC
    }
}
