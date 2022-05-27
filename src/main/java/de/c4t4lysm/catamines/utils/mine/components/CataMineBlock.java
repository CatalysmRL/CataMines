package de.c4t4lysm.catamines.utils.mine.components;

import de.c4t4lysm.catamines.CataMines;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CataMineBlock implements Cloneable, ConfigurationSerializable {

    private Material material;
    private double chance;
    private boolean addLootTable;

    private List<CataMineLootItem> lootTable = new ArrayList<>();

    public CataMineBlock(Material material, double chance) {
        if (!(material.isBlock() || material.isSolid() || chance < 0 || chance > 100)) {
            throw new IllegalArgumentException(CataMines.getInstance().getLangString("Error-Messages.Mine.Invalid-Block-Configuration"));
        }

        this.material = material;
        this.chance = Math.round(chance * 100) / 100d;
    }

    public CataMineBlock(Material material, double chance, boolean addLootTable) {
        if (!(material.isBlock() || material.isSolid() || chance < 0 || chance > 100)) {
            throw new IllegalArgumentException(CataMines.getInstance().getLangString("Error-Messages.Mine.Invalid-Block-Configuration"));
        }

        this.material = material;
        this.chance = Math.round(chance * 100) / 100d;
        this.addLootTable = addLootTable;
    }

    public CataMineBlock(Material material, double chance, List<CataMineLootItem> lootTable) {
        if (!(material.isBlock() || material.isSolid() || chance < 0 || chance > 100)) {
            throw new IllegalArgumentException(CataMines.getInstance().getLangString("Error-Messages.Mine.Invalid-Block-Configuration"));
        }

        this.material = material;
        this.chance = Math.round(chance * 100) / 100d;
        this.lootTable = lootTable;
    }

    public CataMineBlock(Material material, double chance, boolean addLootTable, List<CataMineLootItem> lootTable) {
        if (!(material.isBlock() || material.isSolid() || chance < 0 || chance > 100)) {
            throw new IllegalArgumentException(CataMines.getInstance().getLangString("Error-Messages.Mine.Invalid-Block-Configuration"));
        }

        this.material = material;
        this.chance = Math.round(chance * 100) / 100d;
        this.addLootTable = addLootTable;
        this.lootTable = lootTable;
    }

    @Nullable
    public static CataMineBlock deserialize(Map<String, Object> map) {
        Material material = Material.getMaterial((String) map.get("block"));
        if (material == null) {
            return null;
        }
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
            return new CataMineBlock(material, chance, addLootTable);
        }

        List<CataMineLootItem> lootTable = new ArrayList<>();
        for (Map<String, Object> loot : serializedLootTable) {
            lootTable.add(new CataMineLootItem(ItemStack.deserialize((Map<String, Object>) loot.get("item")), (Double) loot.get("chance")));
        }

        return new CataMineBlock(material, chance, addLootTable, lootTable);
    }

    @Nonnull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("block", material.name());
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

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        if (!material.isBlock() || !material.isSolid()) {
            throw new IllegalArgumentException(CataMines.getInstance().getLangString("Error-Messages.Mine.Material-Not-Solid"));
        }
        this.material = material;
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
