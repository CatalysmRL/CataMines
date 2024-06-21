package me.catalysmrl.catamines.mine.components.region;

import me.catalysmrl.catamines.api.serialization.DeserializationException;
import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import me.catalysmrl.catamines.mine.components.manager.choice.ChoiceManager;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCataMineRegion implements CataMineRegion {

    protected String name;
    protected double chance;
    protected ChoiceManager<CataMineComposition> compositionManager;

    public AbstractCataMineRegion(String name) {
        this.name = name;
        this.compositionManager = new ChoiceManager<>();
    }

    @Override
    public void serialize(ConfigurationSection section) {
        section.set("region-type", getType().toString());
        section.set("name", name);
        section.set("chance", chance);
    }

    public void serializeCompositions(ConfigurationSection compositionsSection) {
        for (int i = 0; i < compositionManager.getChoices().size(); i++) {
            compositionManager.getChoices().get(i).serialize(compositionsSection.createSection("composition-" + i));
        }
    }

    public static List<CataMineComposition> deserializeCompositions(ConfigurationSection compositionsSection) throws DeserializationException {
        List<CataMineComposition> compositionList = new ArrayList<>();
        for (String key : compositionsSection.getKeys(false)) {
            ConfigurationSection compositionSection = compositionsSection.getConfigurationSection(key);
            if (compositionSection == null) continue;
            compositionList.add(CataMineComposition.deserialize(compositionSection));
        }

        return compositionList;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
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

    @Override
    public ChoiceManager<CataMineComposition> getCompositionManager() {
        return compositionManager;
    }

    @Override
    public String toString() {
        return "AbstractCataMineRegion{" +
                "name='" + name + '\'' +
                ", chance=" + chance +
                ", compositionManager=" + compositionManager +
                '}';
    }
}
