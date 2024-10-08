package me.catalysmrl.catamines.api.mine;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.serialization.SectionSerializable;
import me.catalysmrl.catamines.mine.components.manager.choice.ChoiceManager;
import me.catalysmrl.catamines.mine.components.manager.controller.CataMineController;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;

/**
 * A regenerating mine. The user of this interface has precise
 * control over manipulating this mine.
 *
 * @author CatalysmRL
 */
public interface CataMine extends SectionSerializable {

    CataMines getPlugin();

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

    void reset(CataMines plugin);

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

    CataMineController getController();

    void setController(CataMineController controller);

    ChoiceManager<CataMineRegion> getRegionManager();

}
