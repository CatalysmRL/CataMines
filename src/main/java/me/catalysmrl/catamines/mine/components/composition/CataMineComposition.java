package me.catalysmrl.catamines.mine.components.composition;

import com.sk89q.worldedit.function.pattern.RandomPattern;
import me.catalysmrl.catamines.mine.components.manager.choice.Choice;
import me.catalysmrl.catamines.mine.components.manager.choice.Identifiable;
import me.catalysmrl.catamines.mine.reward.Rewardable;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Class representing the composition of a mine.
 * Contains all blocks that make up this upon regeneration.
 *
 * @see CataMineBlock
 * @see com.sk89q.worldedit.function.pattern.RandomPattern
 */

@SerializableAs("CataMineComposition")
public class CataMineComposition implements Rewardable, Identifiable, Choice, ConfigurationSerializable {

    private String name;
    private double chance;

    private final List<CataMineBlock> blocks;
    private RandomPattern randomPattern;

    public CataMineComposition(String name) {
        this.name = name;
        this.blocks = new ArrayList<>();
    }

    public CataMineComposition(String name, final List<CataMineBlock> blocks) {
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
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
    public String toString() {
        return "CataMineComposition{" +
                "name='" + name + '\'' +
                ", blocks=" + blocks +
                ", randomPattern=" + randomPattern +
                '}';
    }
}
