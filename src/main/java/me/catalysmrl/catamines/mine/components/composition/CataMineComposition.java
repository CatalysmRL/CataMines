package me.catalysmrl.catamines.mine.components.composition;

import com.sk89q.worldedit.function.pattern.RandomPattern;
import me.catalysmrl.catamines.mine.abstraction.reward.CataMineReward;
import me.catalysmrl.catamines.mine.abstraction.reward.Rewardable;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Class representing the composition of a mine
 *
 * @see CataMineBlock
 * @see com.sk89q.worldedit.function.pattern.RandomPattern
 */
public class CataMineComposition implements Rewardable, ConfigurationSerializable {

    // The chance to generate this composition
    private double chance;
    private CataMineReward reward;

    private final List<CataMineBlock> blocks;
    private RandomPattern randomPattern;

    public CataMineComposition() {
        this.blocks = new ArrayList<>();
    }

    public CataMineComposition(final List<CataMineBlock> blocks) {
        if (blocks == null) {
            this.blocks = new ArrayList<>();
            return;
        }

        this.blocks = new ArrayList<>(blocks);
        updateRandomPattern();
    }

    public void add(CataMineBlock block) {
        Objects.requireNonNull(block);

        blocks.remove(block);

        double sum = getChanceSum();
        if (sum + block.getChance() > 100) block.setChance(100 - sum);

        blocks.add(block);
        updateRandomPattern();
    }

    public void remove(CataMineBlock block) throws IllegalArgumentException {
        if (!blocks.remove(block))
            throw new IllegalArgumentException("Block is not in composition");
    }

    private void updateRandomPattern() {
        randomPattern = new RandomPattern();
        blocks.stream()
                .filter(b -> b.getChance() > 0)
                .forEach(b -> randomPattern.add(b.getBlockState(), b.getChance()));
    }

    public double getChanceSum() {
        return blocks.stream().mapToDouble(CataMineBlock::getChance).sum();
    }

    public RandomPattern getRandomPattern() {
        return randomPattern;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serializedComp = new LinkedHashMap<>();
        return null;
    }
    public static CataMineComposition deserialize(Map<String, Object> serializedComp) {
        return null;
    }

    @Override
    public double getChance() {
        return chance;
    }

    @Override
    public void setChance(double chance) {
        this.chance = chance;
    }

    @Override
    public CataMineReward getReward() {
        return reward;
    }

    @Override
    public void setReward(CataMineReward reward) {
        this.reward = reward;
    }
}
