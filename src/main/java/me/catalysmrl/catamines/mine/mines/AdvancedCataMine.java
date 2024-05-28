package me.catalysmrl.catamines.mine.mines;

import me.catalysmrl.catamines.mine.abstraction.AbstractCataMine;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("AdvancedCataMine")
public class AdvancedCataMine extends AbstractCataMine {

    public AdvancedCataMine(String name) {
        super(name);
    }

    @Override
    public void serialize(ConfigurationSection section) {

    }

    public static AdvancedCataMine deserialize(ConfigurationSection section) {
        String name = section.getString("name");
        return new AdvancedCataMine(name);
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
