package me.catalysmrl.catamines.mine.mines;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import me.catalysmrl.catamines.mine.abstraction.AbstractCataMine;
import me.catalysmrl.catamines.mine.abstraction.region.AbstractCataMineRegion;
import me.catalysmrl.catamines.mine.components.CataMineType;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class RegionCataMine extends AbstractCataMine {

    AbstractCataMineRegion region;

    public RegionCataMine(String name, AbstractCataMineRegion region) {
        super(name);
        this.region = region;
    }

    @Override
    public CataMineType getMineType() {
        return CataMineType.REGION;
    }

    @Override
    public long getVolume() {
        return 0;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> ser = new LinkedHashMap<>();

        ser.put("name", name);
        ser.put("type", getMineType());


        return ser;
    }

    public static RegionCataMine deserialize(Map<String, Object> ser) {

        String name;
        AbstractCataMineRegion region = null;

        if (ser.containsKey("name")) {
            name = (String) ser.get("name");
        } else {
            return null;
        }

        if (ser.containsKey("region")) {
            
        }

        return new RegionCataMine(name, region);
    }
}
