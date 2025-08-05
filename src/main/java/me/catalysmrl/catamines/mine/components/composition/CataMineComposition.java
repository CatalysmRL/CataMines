package me.catalysmrl.catamines.mine.components.composition;

import com.sk89q.worldedit.function.pattern.RandomPattern;
import me.catalysmrl.catamines.api.serialization.DeserializationException;
import me.catalysmrl.catamines.api.serialization.SectionSerializable;
import me.catalysmrl.catamines.mine.components.manager.choice.Choice;
import me.catalysmrl.catamines.mine.components.manager.choice.Identifiable;
import me.catalysmrl.catamines.mine.reward.Rewardable;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
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

    private List<CataMineBlock> blocks = new ArrayList<>();
    private RandomPattern randomPattern = new RandomPattern();

    public CataMineComposition(String name) {
        this.name = name;
    }

    public boolean canReset() {
        return !blocks.isEmpty();
    }

    public void addBlock(CataMineBlock block) {
        Objects.requireNonNull(block);
        blocks.removeIf(cataMineBlock -> block.getBaseBlock().equals(cataMineBlock.getBaseBlock()));
        blocks.add(block);
        updateRandomPattern();
    }

    public void removeBlock(CataMineBlock block) throws IllegalArgumentException {
        if (!blocks.remove(block))
            throw new IllegalArgumentException("Block is not in composition");
        updateRandomPattern();
    }

    public void clearBlocks() {
        blocks.clear();
        updateRandomPattern();
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

    public List<CataMineBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<CataMineBlock> blocks) {
        this.blocks = blocks;
        updateRandomPattern();
    }

    public RandomPattern getRandomPattern() {
        return randomPattern;
    }

    @Override
    public void serialize(ConfigurationSection section) {
        section.set("name", name);
        section.set("chance", chance);
        ConfigurationSection blocksSection = section.createSection("blocks");
        for (int i = 0; i < blocks.size(); i++) {
            blocks.get(i).serialize(blocksSection.createSection("block-" + i));
        }
    }

    public static CataMineComposition deserialize(ConfigurationSection section) throws DeserializationException {
        String name = section.getString("name");
        if (name == null) throw new DeserializationException("Name not specified");

        double chance = section.getDouble("chance", 0d);

        ConfigurationSection blocksSection = section.getConfigurationSection("blocks");

        List<CataMineBlock> blockList = new ArrayList<>();
        if (blocksSection != null) {
            for (String key : blocksSection.getKeys(false)) {
                ConfigurationSection blockSection = blocksSection.getConfigurationSection(key);
                if (blockSection == null) continue;

                CataMineBlock block = CataMineBlock.deserialize(blockSection);
                blockList.add(block);
            }
        }

        CataMineComposition composition = new CataMineComposition(name);
        composition.setChance(chance);
        composition.setBlocks(blockList);

        return composition;
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
