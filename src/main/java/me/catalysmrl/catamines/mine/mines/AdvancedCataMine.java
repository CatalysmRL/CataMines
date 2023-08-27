package me.catalysmrl.catamines.mine.mines;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import me.catalysmrl.catamines.mine.abstraction.AbstractCataMine;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class AdvancedCataMine extends AbstractCataMine {

    public AdvancedCataMine(String name) {
        super(name);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> ser = new LinkedHashMap<>();

        ser.put("name", name);
        Clipboard


        return ser;
    }


    public static AdvancedCataMine deserialize(Map<String, Object> ser) {

        String name;

        if (ser.containsKey("name")) {
            name = (String) ser.get("name");
        } else {
            return null;
        }

        return new AdvancedCataMine(name);
    }
}
