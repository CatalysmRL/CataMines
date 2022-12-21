package me.catalysmrl.catamines.mine.mines;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import me.catalysmrl.catamines.mine.abstraction.AbstractCataMine;
import me.catalysmrl.catamines.mine.components.CataMineType;
import me.catalysmrl.catamines.utils.worldedit.WorldEditUtils;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SchematicCataMine extends AbstractCataMine {

    String schemName;
    Location location;
    Clipboard clipboard;

    public SchematicCataMine(String name, String schemName, Location location) {
        super(name);
        this.schemName = schemName;
        this.location = location;
        this.clipboard = WorldEditUtils.loadSchematic(schemName);
    }

    @Override
    public void fillRegion() {
        WorldEditUtils.pasteSchematic(clipboard, location);
    }

    @Override
    public CataMineType getMineType() {
        return CataMineType.SCHEMATIC;
    }

    @Override
    public long getVolume() {
        return clipboard.getVolume();
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        return null;
    }
}
