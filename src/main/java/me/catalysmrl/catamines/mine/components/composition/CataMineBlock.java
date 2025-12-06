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

    private String blockString;
    private BaseBlock baseBlock;
    private double chance;

    private DropType dropType;
    private List<CataMineItem> items;

    public CataMineBlock(String blockString, double chance) throws InputParseException {
        this(blockString, chance, DropType.CUSTOM);
    }

    public CataMineBlock(String blockString, double chance, DropType dropType) throws InputParseException {
        this(blockString, chance, dropType, new ArrayList<>());
    }

    public CataMineBlock(String blockString, double chance, DropType dropType, List<CataMineItem> items) throws InputParseException {
        if (chance < 0 || chance > 100) {
            throw new IllegalArgumentException(Message.SET_INVALID_CHANCE.getKey());
        }

        this.blockString = blockString;
        this.baseBlock = BaseBlockParser.parse(blockString);
        this.chance = Math.round(chance * 100) / 100d;
        this.dropType = dropType;
        this.items = items;
    }

    @Override
    public void serialize(ConfigurationSection section) {
        section.set("block", blockString);
        section.set("chance", chance);
        section.set("drop-type", dropType.toString());

        ConfigurationSection lootTableSection = section.createSection("loot-table");
        for (int i = 0; i < items.size(); i++) {
            items.get(i).serialize(lootTableSection.createSection("item-" + i));
        }
    }

    public static CataMineBlock deserialize(ConfigurationSection section) throws DeserializationException {

        String blockString = section.getString("block");
        if (blockString == null)
            throw new DeserializationException("Missing 'block' path");

        double chance = section.getDouble("chance", 0d);

        DropType dropType = DropType.valueOf(section.getString("drop-type", "CUSTOM"));

        ConfigurationSection itemsSection = section.getConfigurationSection("loot-table");
        List<CataMineItem> itemList = new ArrayList<>();
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection == null)
                    continue;
                itemList.add(CataMineItem.deserialize(itemSection));
            }
        }

        try {
            return new CataMineBlock(blockString, chance, dropType, itemList);
        } catch (InputParseException e) {
            throw new DeserializationException("Invalid block input: " + e.getMessage());
        }
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
