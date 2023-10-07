package me.catalysmrl.catamines.mine.components.region;

import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import me.catalysmrl.catamines.mine.components.reward.Rewardable;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.List;
import java.util.Optional;

public interface CataMineRegion extends Rewardable, ConfigurationSerializable {

    String getName();

    void setName(String name);

    void fill();

    RegionType getType();

    default Optional<CataMineComposition> getComposition(String name) {
        return getCompositions().stream().filter(composition -> composition.getName().equals(name)).findFirst();
    }

    List<CataMineComposition> getCompositions();

    long getVolume();

    enum RegionType {
        SELECTION,
        SCHEMATIC
    }
}
