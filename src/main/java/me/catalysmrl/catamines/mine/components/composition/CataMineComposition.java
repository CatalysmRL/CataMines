package me.catalysmrl.catamines.mine.components.composition;

import com.sk89q.worldedit.function.pattern.RandomPattern;
import me.catalysmrl.catamines.mine.components.reward.Rewardable;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Class representing the composition of a mine.
 * Contains all blocks that make up this upon regeneration.
 *
 * @see CataMineBlock
 * @see com.sk89q.worldedit.function.pattern.RandomPattern
 */
public class CataMineComposition implements Rewardable, ConfigurationSerializable {

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

        blocks.removeIf(cataMineBlock -> block.getBaseBlock().equals(cataMineBlock.getBaseBlock()));

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
                .forEach(b -> randomPattern.add(b.getBaseBlock(), b.getChance()));
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
}
