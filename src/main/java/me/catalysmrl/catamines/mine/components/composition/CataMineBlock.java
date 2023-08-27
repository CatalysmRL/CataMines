package me.catalysmrl.catamines.mine.components.composition;

import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import me.catalysmrl.catamines.mine.components.composition.drop.CataMineItem;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CataMineBlock implements ConfigurationSerializable {

    private BaseBlock baseBlock;
    private double chance;

    private DropType dropType;
    private List<CataMineItem> drops;

    public CataMineBlock(BaseBlock baseBlock, double chance) {
        this(baseBlock, chance, DropType.CUSTOM);
    }

    public CataMineBlock(BaseBlock baseBlock, double chance, DropType dropType) {
        this(baseBlock, chance, dropType, new ArrayList<>());
    }

    public CataMineBlock(BaseBlock baseBlock, double chance, DropType dropType, List<CataMineItem> drops) {
        if (chance < 0 || chance > 100) {
            throw new IllegalArgumentException(Message.SET_INVALID_CHANCE.getMessage());
        }

        this.baseBlock = baseBlock;
        this.chance = Math.round(chance * 100) / 100d;
        this.dropType = dropType;
        this.drops = drops;
    }

    public static CataMineBlock deserialize(Map<String, Object> map) {

        BaseBlock baseBlock = BlockState.get((String) map.get("block")).toBaseBlock();

        double chance = 0d;
        if (map.containsKey("chance")) {
            chance = (double) map.get("chance");
        }

        DropType dropType = DropType.CUSTOM;
        if (map.containsKey("dropType")) {
            dropType = DropType.valueOf((String) map.get("dropType"));
        }

        ArrayList<Map<String, Object>> serializedLootTable = new ArrayList<>();
        if (map.containsKey("lootTable")) {
            serializedLootTable = (ArrayList<Map<String, Object>>) map.get("lootTable");
        }
        if (serializedLootTable.isEmpty()) {
            return new CataMineBlock(baseBlock, chance);
        }

        List<CataMineItem> drops = new ArrayList<>();
        for (Map<String, Object> loot : serializedLootTable) {
            drops.add(CataMineItem.deserialize(loot));
        }

        return new CataMineBlock(baseBlock, chance, dropType, drops);
    }

    @Nonnull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("block", baseBlock.toString());
        result.put("chance", chance);
        result.put("dropType", dropType);

        ArrayList<Map<String, Object>> mappedLootTable = new ArrayList<>();

        for (CataMineItem drop : drops) {
            mappedLootTable.add(drop.serialize());
        }

        result.put("rewards", mappedLootTable);

        return result;
    }

    public BaseBlock getBaseBlock() {
        return baseBlock;
    }

    public void setBaseBlock(BaseBlock baseBlock) {
        this.baseBlock = baseBlock;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        if (chance < 0 || chance > 100) {
            throw new IllegalArgumentException("Invalid chance");
        }
        this.chance = Math.round(chance * 100) / 100d;
    }

    public DropType getDropType() {
        return dropType;
    }

    public void setDropType(DropType dropType) {
        this.dropType = dropType;
    }

    public List<CataMineItem> getDrops() {
        return drops;
    }

    public void setDrops(List<CataMineItem> drops) {
        this.drops = drops;
    }

    public enum DropType {
        CUSTOM, ALL, SINGLE;
    }
}
