package me.catalysmrl.catamines.mine.reward.rewardaction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.configuration.ConfigurationSection;

public final class RewardActionRegistry {

    private static final Map<String, Function<ConfigurationSection, RewardAction>> REGISTRY = new HashMap<>();

    private RewardActionRegistry() {}

    public static void register(String key, Function<ConfigurationSection, RewardAction> factory) {
        REGISTRY.put(key.toLowerCase(), factory);
    }

    public static RewardAction create(ConfigurationSection section) {
        
        String type = section.getString("type");
        if (type == null) throw new IllegalArgumentException("RewardAction is missing 'type'!");

        Function<ConfigurationSection, RewardAction> factory = REGISTRY.get(type.toLowerCase());

        if (factory == null) throw new IllegalArgumentException("Unknown RewardAction type: " + type);

        return factory.apply(section);
    }

}