package me.catalysmrl.catamines.mine.reward;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import me.catalysmrl.catamines.api.serialization.SectionSerializable;
import me.catalysmrl.catamines.mine.reward.rewardaction.RewardAction;
import me.catalysmrl.catamines.mine.reward.rewardaction.RewardActionRegistry;

public final class RewardDefinition implements SectionSerializable{

    private final String id;
    private final double chance;
    private final RollMode rollMode;
    private final List<RewardAction> actions;

    public RewardDefinition(String id, double chance, RollMode rollMode, List<RewardAction> actions) {
        this.id = id;
        this.chance = chance;
        this.rollMode = rollMode;
        this.actions = actions;
    }

    @Override
    public void serialize(ConfigurationSection section) {
        
    }

    public static RewardDefinition deserialize(ConfigurationSection section) {

        String id = section.getString("id", "reward-" + UUID.randomUUID());
        double chance = section.getDouble("chance", 100d);
        RollMode rollMode = RollMode.valueOf(section.getString("roll-mode", "PER_REWARD").toUpperCase());

        List<RewardAction> actions = new ArrayList<>();

        ConfigurationSection actionsSection = section.getConfigurationSection("actions");
        if (actionsSection == null) throw new IllegalArgumentException("Reward '" + id + "' has no actions.");

        for (String key : actionsSection.getKeys(false)) {
            ConfigurationSection actionConfig = actionsSection.getConfigurationSection(key);
            actions.add(RewardActionRegistry.create(actionConfig));
        }

        return new RewardDefinition(id, chance, rollMode, actions);
    }

    public String getId() {
        return id;
    }

    public double getChance() {
        return chance;
    }

    public RollMode getRollMode() {
        return rollMode;
    }

    public List<RewardAction> getActions() {
        return actions;
    }

}
