package me.catalysmrl.catamines.api.mine;

/**
 * Marker interface for entities that can be targeted by the mine path syntax.
 * This includes mines, regions, compositions, and potentially blocks in the
 * future.
 * <p>
 * The path syntax is: {@code mine:region:composition:block}
 * <p>
 * Not all targetable entities are {@link PropertyHolder}s. For example, blocks
 * may be targetable but do not have configurable properties.
 */
public interface Targetable {

    /**
     * Gets the human-readable name of this targetable entity.
     * 
     * @return the name of this entity
     */
    String getName();
}
