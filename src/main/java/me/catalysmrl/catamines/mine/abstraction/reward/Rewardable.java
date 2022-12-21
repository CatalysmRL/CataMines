package me.catalysmrl.catamines.mine.abstraction.reward;

public interface Rewardable {

    double getChance();

    void setChance(double chance);

    CataMineReward getReward();

    void setReward(CataMineReward reward);
}
