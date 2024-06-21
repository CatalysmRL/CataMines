package me.catalysmrl.catamines.mine.mines;

import me.catalysmrl.catamines.api.serialization.DeserializationException;
import me.catalysmrl.catamines.mine.abstraction.AbstractCataMine;
import me.catalysmrl.catamines.mine.components.manager.controller.CataMineController;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.mine.components.region.impl.SchematicRegion;
import me.catalysmrl.catamines.mine.components.region.impl.SelectionRegion;
import org.bukkit.configuration.ConfigurationSection;

public class AdvancedCataMine extends AbstractCataMine {

    public AdvancedCataMine(String name) {
        super(name);
    }

    @Override
    public void serialize(ConfigurationSection section) {
        section.set("name", name);
        section.set("display-name", displayName);
        controller.serialize(section.createSection("controller"));
        ConfigurationSection regionsSection = section.createSection("regions");
        for (int i = 0; i < regionManager.getChoices().size(); i++) {
            regionManager.getChoices().get(i).serialize(regionsSection.createSection("region-" + i));
        }
    }

    public static AdvancedCataMine deserialize(ConfigurationSection section) throws DeserializationException {
        String name = section.getString("name");
        if (name == null) throw new DeserializationException();

        AdvancedCataMine cataMine = new AdvancedCataMine(name);

        String displayName = section.getString("display-name", name);
        cataMine.setDisplayName(displayName);

        ConfigurationSection controllerSection = section.getConfigurationSection("controller");
        if (controllerSection == null) throw new DeserializationException();

        cataMine.controller = CataMineController.deserialize(controllerSection, cataMine);

        ConfigurationSection regionsSection = section.getConfigurationSection("regions");
        if (regionsSection == null) throw new DeserializationException();

        for (String key : regionsSection.getKeys(false)) {
            ConfigurationSection regionSection = regionsSection.getConfigurationSection(key);
            if (regionSection == null) continue;

            CataMineRegion.RegionType regionType = CataMineRegion.RegionType.valueOf(regionSection.getString("region-type"));
            if (regionType == CataMineRegion.RegionType.SELECTION) {
                cataMine.getRegionManager().add(SelectionRegion.deserialize(regionSection));
            } else if (regionType == CataMineRegion.RegionType.SCHEMATIC) {
                cataMine.getRegionManager().add(SchematicRegion.deserialize(regionSection));
            }
        }

        return cataMine;
    }

    @Override
    public String toString() {
        return "AdvancedCataMine{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", regions=" + regionManager +
                '}';
    }

}
