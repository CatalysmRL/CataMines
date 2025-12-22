package me.catalysmrl.catamines.mine.reward;

import java.util.List;

public final class RewardContainer {

    private final boolean override;
    private final List<RewardDefinition> rewards;

    RewardContainer(boolean override, List<RewardDefinition> rewards) {
        this.override = override;
        this.rewards = rewards;
    }

    public boolean isOverride() {
        return override;
    }

    public List<RewardDefinition> getRewards() {
        return rewards;
    }

}
