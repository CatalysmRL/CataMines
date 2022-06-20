package de.c4t4lysm.catamines.utils.mine.components;

import de.c4t4lysm.catamines.CataMines;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CataMineBlock implements Cloneable, ConfigurationSerializable {

    private BlockData blockData;
    private double chance;
    private boolean addLootTable;

    private List<CataMineLootItem> lootTable = new ArrayList<>();

    public CataMineBlock(BlockData blockData, double chance) {
        if (!blockData.getMaterial().isBlock() || chance < 0 || chance > 100) {
            throw new IllegalArgumentException(CataMines.getInstance().getLangString("Error-Messages.Mine.Invalid-Block-Configuration"));
        }

        this.blockData = blockData;
        this.chance = Math.round(chance * 100) / 100d;
    }

    public CataMineBlock(BlockData blockData, double chance, boolean addLootTable) {
        if (!blockData.getMaterial().isBlock() || chance < 0 || chance > 100) {
            throw new IllegalArgumentException(CataMines.getInstance().getLangString("Error-Messages.Mine.Invalid-Block-Configuration"));
        }

        this.blockData = blockData;
        this.chance = Math.round(chance * 100) / 100d;
        this.addLootTable = addLootTable;
    }

    public CataMineBlock(BlockData blockData, double chance, List<CataMineLootItem> lootTable) {
        if (!blockData.getMaterial().isBlock() || chance < 0 || chance > 100) {
            throw new IllegalArgumentException(CataMines.getInstance().getLangString("Error-Messages.Mine.Invalid-Block-Configuration"));
        }

        this.blockData = blockData;
        this.chance = Math.round(chance * 100) / 100d;
        this.lootTable = lootTable;
    }

    public CataMineBlock(BlockData blockData, double chance, boolean addLootTable, List<CataMineLootItem> lootTable) {
        if (!blockData.getMaterial().isBlock() || chance < 0 || chance > 100) {
            throw new IllegalArgumentException(CataMines.getInstance().getLangString("Error-Messages.Mine.Invalid-Block-Configuration"));
        }

        this.blockData = blockData;
        this.chance = Math.round(chance * 100) / 100d;
        this.addLootTable = addLootTable;
        this.lootTable = lootTable;
    }

    public static CataMineBlock deserialize(Map<String, Object> map) {
        BlockData blockData = Bukkit.createBlockData(((String) map.get("block")).toLowerCase());
        double chance = 0d;
        if (map.containsKey("chance")) {
            chance = (double) map.get("chance");
        }
        boolean addLootTable = false;
        if (map.containsKey("addLootTable")) {
            addLootTable = (boolean) map.get("addLootTable");
        }

        ArrayList<Map<String, Object>> serializedLootTable = new ArrayList<>();
        if (map.containsKey("lootTable")) {
            serializedLootTable = (ArrayList<Map<String, Object>>) map.get("lootTable");
        }
        if (serializedLootTable.isEmpty()) {
            return new CataMineBlock(blockData, chance, addLootTable);
        }

        List<CataMineLootItem> lootTable = new ArrayList<>();
        for (Map<String, Object> loot : serializedLootTable) {
            lootTable.add(new CataMineLootItem(ItemStack.deserialize((Map<String, Object>) loot.get("item")), (Double) loot.get("chance")));
        }

        return new CataMineBlock(blockData, chance, addLootTable, lootTable);
    }

    @Nonnull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("block", blockData.getAsString(false));
        result.put("chance", chance);
        result.put("addLootTable", addLootTable);

        ArrayList<Map<String, Object>> mappedLootTable = new ArrayList<>();

        for (CataMineLootItem lootItem : lootTable) {
            Map<String, Object> mappedLoot = new LinkedHashMap<>();
            ItemStack itemStack = lootItem.getItem();
            double chance = lootItem.getChance();
            mappedLoot.put("item", itemStack.serialize());
            mappedLoot.put("chance", chance);
            mappedLootTable.add(mappedLoot);
        }

        result.put("lootTable", mappedLootTable);

        return result;
    }

    public BlockData getBlockData() {
        return blockData;
    }

    public void setBlockData(BlockData blockData) {
        if (!blockData.getMaterial().isBlock()) {
            throw new IllegalArgumentException(CataMines.getInstance().getLangString("Error-Messages.Mine.Material-Not-Solid"));
        }
        this.blockData = blockData;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        if (chance < 0 || chance > 100) {
            throw new IllegalArgumentException(CataMines.getInstance().getLangString("Error-Messages.Mine.Invalid-Chance"));
        }
        this.chance = Math.round(chance * 100) / 100d;
    }

    public boolean isAddLootTable() {
        return addLootTable;
    }

    public void setAddLootTable(boolean addLootTable) {
        this.addLootTable = addLootTable;
    }

    public List<CataMineLootItem> getLootTable() {
        return lootTable;
    }

    public void setLootTable(List<CataMineLootItem> lootTable) {
        this.lootTable = lootTable;
    }

    @Override
    public CataMineBlock clone() {
        try {
            return (CataMineBlock) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
