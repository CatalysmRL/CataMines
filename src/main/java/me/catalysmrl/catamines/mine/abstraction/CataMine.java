package me.catalysmrl.catamines.mine.abstraction;

import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.List;
import java.util.Optional;

/**
 * A regenerating mine. The user of this interface has precise
 * control over manipulating this mine.
 *
 * @author CatalysmRL
 */
public interface CataMine extends ConfigurationSerializable {

    /**
     * Ticks the mine and notifys it to perform various actions
     * depending on the configuration of the mine. This method may
     * trigger to decrement the countdown, reset the mine, broadcast
     * messages to nearby players. This method gets called every second
     * by the underlying {@link me.catalysmrl.catamines.managers.MineManager}
     * and gets triggered by a BlockBreakListener if the mine is configured
     * to reset by percentage
     * <p>
     * Calling this method async may have unwanted side effects.
     *
     * @see me.catalysmrl.catamines.managers.MineManager
     */
    void tick();

    void reset();

    /**
     * Gets the name of the mine which is used as a unique identifier.
     *
     * @return the mines name
     */
    String getName();

    /**
     * Sets the name of the mine which is used as a unique identifier.
     * This should usually not be set directly since it may cause
     * unwanted side effects such as duplicate files etc.
     * To rename a mine, use {@link me.catalysmrl.catamines.managers.MineManager}
     *
     * @param name the unique identifiable name
     * @see me.catalysmrl.catamines.managers.MineManager
     */
    void setName(String name);

    String getDisplayName();

    void setDisplayName(String displayName);

    List<CataMineRegion> getRegions();

    default Optional<CataMineRegion> getRegion(String name) {
        return getRegions().stream().filter(region -> region.getName().equals(name)).findFirst();
    }
}
