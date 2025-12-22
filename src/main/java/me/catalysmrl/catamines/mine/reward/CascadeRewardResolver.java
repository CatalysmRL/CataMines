package me.catalysmrl.catamines.mine.reward;

import java.util.ArrayList;
import java.util.List;

import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.mine.components.composition.CataMineBlock;
import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;

public final class CascadeRewardResolver {

    public static List<RewardDefinition> resolveRewards(String triggerId, CataMine mine, CataMineRegion region, CataMineComposition composition, CataMineBlock block) {

        List<RewardDefinition> result = new ArrayList<>();

        // 1️⃣ Block Level
        if (block != null && block.hasRewardsFor(triggerId)) {
            RewardContainer c = block.getRewardsFor(triggerId);
            result.addAll(c.getRewards());
            if (c.isOverride()) return result;
        }

        // 2️⃣ Composition Level
        if (composition != null && composition.hasRewardsFor(triggerId)) {
            RewardContainer c = composition.getRewardsFor(triggerId);
            result.addAll(c.getRewards());
            if (c.isOverride()) return result;
        }

        // 3️⃣ Region Level
        if (region != null && region.hasRewardsFor(triggerId)) {
            RewardContainer c = region.getRewardsFor(triggerId);
            result.addAll(c.getRewards());
            if (c.isOverride()) return result;
        }

        // 4️⃣ Mine Level
        if (mine != null && mine.hasRewardsFor(triggerId)) {
            RewardContainer c = mine.getRewardsFor(triggerId);
            result.addAll(c.getRewards());
        }

        return result;

    }

}
