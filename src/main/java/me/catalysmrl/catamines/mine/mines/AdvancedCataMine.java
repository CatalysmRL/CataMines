package me.catalysmrl.catamines.mine.mines;

import me.catalysmrl.catamines.mine.abstraction.AbstractCataMine;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

@SerializableAs("AdvancedCataMine")
public class AdvancedCataMine extends AbstractCataMine {

    public AdvancedCataMine(String name) {
        super(name);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> ser = new LinkedHashMap<>();
        ser.put("name", name);
        ser.put("regions", regionManager.getChoices());

        return ser;
    }


    public static AdvancedCataMine deserialize(Map<String, Object> ser) {

        String name = (String) ser.get("name");


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
