package me.catalysmrl.catamines.mine.components.composition;

import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.world.block.BaseBlock;
import me.catalysmrl.catamines.api.serialization.DeserializationException;
import me.catalysmrl.catamines.api.serialization.SectionSerializable;
import me.catalysmrl.catamines.mine.components.composition.drop.CataMineItem;
import me.catalysmrl.catamines.mine.components.manager.choice.Choice;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.worldedit.BaseBlockParser;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class CataMineBlock implements Choice, SectionSerializable {

    private BaseBlock baseBlock;
    private double chance;

    private DropType dropType;
    private List<CataMineItem> items;

    public CataMineBlock(BaseBlock baseBlock, double chance) {
        this(baseBlock, chance, DropType.CUSTOM);
    }

    public CataMineBlock(BaseBlock baseBlock, double chance, DropType dropType) {
        this(baseBlock, chance, dropType, new ArrayList<>());
    }

    public CataMineBlock(BaseBlock baseBlock, double chance, DropType dropType, List<CataMineItem> items) {
        if (chance < 0 || chance > 100) {
            throw new IllegalArgumentException(Message.SET_INVALID_CHANCE.getMessage());
        }

        this.baseBlock = baseBlock;
        this.chance = Math.round(chance * 100) / 100d;
        this.dropType = dropType;
        this.items = items;
    }

    @Override
    public void serialize(ConfigurationSection section) {
        section.set("block", baseBlock.toString());
        section.set("chance", chance);
        section.set("drop_type", dropType);

        for (int i = 0; i < items.size(); i++) {
            ConfigurationSection itemSection = section.createSection("item_" + i);
            CataMineItem item = items.get(i);
            item.serialize(itemSection);
        }
    }

    public static CataMineBlock deserialize(ConfigurationSection section) throws DeserializationException {

        String blockString = section.getString("block");
        if (blockString == null) throw new DeserializationException("Missing 'block' path");

        BaseBlock baseBlock;
        try {
            baseBlock = BaseBlockParser.parseInput(blockString);
        } catch (InputParseException exception) {
            throw new DeserializationException("Could not deserialize " + blockString, exception);
        }

        double chance = section.getDouble("chance", 0d);

        DropType dropType = DropType.valueOf(section.getString("drop_type", "CUSTOM"));

        ConfigurationSection itemsSection = section.getConfigurationSection("loot_table");
        List<CataMineItem> itemList = new ArrayList<>();
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection == null) continue;
                itemList.add(CataMineItem.deserialize(itemSection));
            }
        }

        return new CataMineBlock(baseBlock, chance, dropType, itemList);
    }

    public BaseBlock getBaseBlock() {
        return baseBlock;
    }

    public void setBaseBlock(BaseBlock baseBlock) {
        this.baseBlock = baseBlock;
    }

    @Override
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

    public List<CataMineItem> getItems() {
        return items;
    }

    public void setItems(List<CataMineItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "CataMineBlock{" +
                "baseBlock=" + baseBlock +
                ", chance=" + chance +
                ", dropType=" + dropType +
                ", drops=" + items +
                '}';
    }

    public enum DropType {
        CUSTOM, ALL, SINGLE;
    }
}
