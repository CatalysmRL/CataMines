package me.catalysmrl.catamines.mine.components.composition;

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

    private BlockState blockState;
    private double chance;

    private DropType dropType;
    private List<CataMineItem> drops;

    public CataMineBlock(BlockState blockState, double chance) {
        this(blockState, chance, DropType.CUSTOM);
    }

    public CataMineBlock(BlockState blockState, double chance, DropType dropType) {
        this(blockState, chance, dropType, new ArrayList<>());
    }

    public CataMineBlock(BlockState blockState, double chance, DropType dropType, List<CataMineItem> drops) {
        if (chance < 0 || chance > 100) {
            throw new IllegalArgumentException(Message.SET_INVALID_CHANCE.getMessage());
        }

        this.blockState = blockState;
        this.chance = Math.round(chance * 100) / 100d;
        this.drops = drops;
    }

    public static CataMineBlock deserialize(Map<String, Object> map) {

        BlockState blockState = BlockState.get((String) map.get("block"));

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
            return new CataMineBlock(blockState, chance);
        }

        List<CataMineItem> drops = new ArrayList<>();
        for (Map<String, Object> loot : serializedLootTable) {
            drops.add(CataMineItem.deserialize(loot));
        }

        return new CataMineBlock(blockState, chance, dropType, drops);
    }

    @Nonnull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("block", blockState.toString());
        result.put("chance", chance);
        result.put("dropType", dropType);

        ArrayList<Map<String, Object>> mappedLootTable = new ArrayList<>();

        for (CataMineItem drop : drops) {
            mappedLootTable.add(drop.serialize());
        }

        result.put("rewards", mappedLootTable);

        return result;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public void setBlockState(BlockState blockState) {
        this.blockState = blockState;
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

    public List<CataMineItem> getDrops() {
        return drops;
    }

    public void setDrops(List<CataMineItem> drops) {
        this.drops = drops;
    }

    private enum DropType {
        CUSTOM, ALL, SINGLE;
    }
}
