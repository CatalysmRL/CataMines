package me.catalysmrl.catamines.mine.reward;

import java.util.List;
import java.util.Random;

import me.catalysmrl.catamines.mine.reward.rewardaction.RewardAction;
import me.catalysmrl.catamines.mine.reward.rewardaction.RewardContext;

public final class RewardExecutor {

    private static final Random RANDOM = new Random();

    private RewardExecutor() {}

    public static void executeAll(List<RewardDefinition> rewards, RewardContext context) {
        if (rewards == null || rewards.isEmpty()) return;

        for (RewardDefinition reward : rewards) {
            boolean success = shouldExecute(reward);

            if (!success) continue;

            for (RewardAction action : reward.getActions()) {
                try {
                    action.execute(context);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

            if (reward.getRollMode() == RollMode.FIRST_SUCCESS) break;
        }
    }

    private static boolean shouldExecute(RewardDefinition reward) {
        RollMode mode = reward.getRollMode();

        if (mode == RollMode.ALL) return true;

        double chance = reward.getChance();

        return RANDOM.nextDouble() * 100d <= chance;
    }

}
