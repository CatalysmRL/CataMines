package de.c4t4lysm.catamines.utils.mine.components;

import org.bukkit.Material;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CataMineRandomPattern {

    private final Random random = new Random();
    private final List<CataMineBlock> mineBlocks = new ArrayList<>();
    private double maxWeight = 0;

    public void add(@Nonnull CataMineBlock block) {
        mineBlocks.add(block);
        maxWeight += block.getChance();
    }

    public CataMineBlock nextBlock() {
        double r = random.nextDouble();
        double offset = 0;

        for (CataMineBlock block : mineBlocks) {
            if (r <= (offset + block.getChance()) / maxWeight) {
                return block;
            }
            offset += block.getChance();
        }

        throw new RuntimeException("CataMineRandomPatternError");
    }

    public Material nextMaterial() {
        double r = random.nextDouble();
        double offset = 0;

        for (CataMineBlock block : mineBlocks) {
            if (r <= (offset + block.getChance()) / maxWeight) {
                return block.getBlockData().getMaterial();
            }
            offset += block.getChance();
        }

        throw new RuntimeException("CataMineRandomPatternError");
    }

}
