package me.catalysmrl.catamines.rewards.targeters;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.Collection;

public interface CataEntityTargeter {

    Collection<Entity> getEntities(Location origin);

}
