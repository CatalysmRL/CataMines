package me.catalysmrl.catamines.mine.reward;

public interface RewardHolder {

    boolean hasRewardsFor(String triggerId);

    RewardContainer getRewardsFor(String triggerId);

}
