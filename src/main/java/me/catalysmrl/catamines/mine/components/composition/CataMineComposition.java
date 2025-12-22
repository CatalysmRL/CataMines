package me.catalysmrl.catamines.mine.components.composition;

import com.sk89q.worldedit.function.pattern.RandomPattern;
import me.catalysmrl.catamines.api.serialization.DeserializationException;
import me.catalysmrl.catamines.api.serialization.SectionSerializable;
import me.catalysmrl.catamines.mine.components.MineFlags;
import me.catalysmrl.catamines.mine.components.manager.choice.Choice;
import me.catalysmrl.catamines.mine.components.manager.choice.Identifiable;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.mine.reward.RewardContainer;
import me.catalysmrl.catamines.mine.reward.RewardHolder;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.catalysmrl.catamines.api.mine.Flag;
import me.catalysmrl.catamines.api.mine.PropertyHolder;
import me.catalysmrl.catamines.api.mine.Targetable;

public class CataMineComposition
        implements Identifiable, Choice, SectionSerializable, PropertyHolder, Targetable, RewardHolder, Cloneable {

    private CataMineRegion region;

    private String name;
    private double chance = 100.0d;

    private List<CataMineBlock> blocks = new ArrayList<>();
    private RandomPattern randomPattern = new RandomPattern();

    private MineFlags flags = new MineFlags();

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

    public void setRegion(CataMineRegion region) {
        this.region = region;
    }

    @Override
    public <T> T getFlag(Flag<T> flag) {
        return flags.get(flag);
    }

    @Override
    public <T> void setFlag(Flag<T> flag, T value) {
        flags.set(flag, value);
    }

    @Override
    public boolean hasFlag(Flag<?> flag) {
        return flags.get(flag) != null;
    }

    @Override
    public PropertyHolder getParent() {
        return region;
    }

    @Override
    public void serialize(ConfigurationSection section) {
        section.set("name", name);
        section.set("chance", chance);
        flags.serialize(section.createSection("flags"));
        ConfigurationSection blocksSection = section.createSection("blocks");
        for (int i = 0; i < blocks.size(); i++) {
            blocks.get(i).serialize(blocksSection.createSection("block-" + i));
        }
    }

    public static CataMineComposition deserialize(ConfigurationSection section) throws DeserializationException {
        String name = section.getString("name");
        if (name == null)
            throw new DeserializationException("Name not specified");

        double chance = section.getDouble("chance", 0d);

        ConfigurationSection blocksSection = section.getConfigurationSection("blocks");

        List<CataMineBlock> blockList = new ArrayList<>();
        if (blocksSection != null) {
            for (String key : blocksSection.getKeys(false)) {
                ConfigurationSection blockSection = blocksSection.getConfigurationSection(key);
                if (blockSection == null)
                    continue;

                CataMineBlock block = CataMineBlock.deserialize(blockSection);
                blockList.add(block);
            }
        }

        CataMineComposition composition = new CataMineComposition(name);
        composition.setChance(chance);
        composition.setBlocks(blockList);

        if (section.contains("flags")) {
            composition.flags = MineFlags.deserialize(section.getConfigurationSection("flags"));
        }

        return composition;
    }

    @Override
    public boolean hasRewardsFor(String triggerId) {
        return false;
    }

    @Override
    public RewardContainer getRewardsFor(String triggerId) {
        return null;
    }

    @Override
    public CataMineComposition clone() {
        try {
            CataMineComposition clone = (CataMineComposition) super.clone();
            clone.flags = this.flags.clone();
            clone.blocks = new ArrayList<>(this.blocks); // shallow copy of list
            clone.randomPattern = new RandomPattern();
            // Rebuild pattern from cloned blocks
            clone.blocks.forEach(b -> {
                if (b.getChance() > 0) {
                    clone.randomPattern.add(b.getBaseBlock(), b.getChance());
                }
            });
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
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
