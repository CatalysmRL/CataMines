package me.catalysmrl.catamines.mine.components.composition;

import com.sk89q.worldedit.function.pattern.RandomPattern;
import me.catalysmrl.catamines.api.serialization.SectionSerializable;
import me.catalysmrl.catamines.mine.components.manager.choice.Choice;
import me.catalysmrl.catamines.mine.components.manager.choice.Identifiable;
import me.catalysmrl.catamines.mine.reward.Rewardable;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class representing the composition of a mine.
 * Contains all blocks that make up this upon regeneration.
 *
 * @see CataMineBlock
 * @see com.sk89q.worldedit.function.pattern.RandomPattern
 */

public class CataMineComposition implements Rewardable, Identifiable, Choice, SectionSerializable {

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

    @Override
    public void serialize(ConfigurationSection section) {
        section.set("name", name);
        section.set("chance", chance);
        section.set("blocks", blocks);
    }

    public static CataMineComposition deserialize(Map<String, Object> serializedComp) {
        return null;
    }

    @Override
    public String toString() {
        return "CataMineComposition{" +
                "name='" + name + '\'' +
                ", chance=" + chance +
                ", blocks=" + blocks +
                ", randomPattern=" + randomPattern +
                '}';
    }
}
