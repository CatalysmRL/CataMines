package me.catalysmrl.catamines.utils.worldedit;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.session.ClipboardHolder;
import me.catalysmrl.catamines.CataMines;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WorldEditUtils {
    public static RegionSelector getSelector(Player player) {
        return WorldEditPlugin.getInstance().getSession(player).getRegionSelector(BukkitAdapter.adapt(player.getWorld()));
    }

    public static void pasteSchematic(Clipboard clipboard, Location location) {
        if (clipboard == null)
            throw new IllegalArgumentException("Invalid Clipboard");

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(location.getWorld()))) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ()))
                    .ignoreAirBlocks(true)
                    .copyEntities(false)
                    .build();
            Operations.complete(operation);
        } catch (WorldEditException e) {
            e.printStackTrace();
        }
    }

    public static Clipboard loadSchematic(String schemName) {
        File file = new File(CataMines.getInstance().getDataFolder() + "/schematics/" + schemName);

        Clipboard clipboard = null;

        ClipboardFormat format = ClipboardFormats.findByFile(file);

        if (format == null) {
            throw new IllegalArgumentException("Could not load schematic " + file.getPath());
        }

        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            clipboard = reader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return clipboard;
    }

    public static BlockVector3 unparseBlockVector3(String input) {
        String[] values = input.split(",");
        if (values.length != 3) {
            throw new IllegalArgumentException("Input must contain 3 comma-separated values");
        }
        int x, y, z;
        try {
            x = Integer.parseInt(values[0]);
            y = Integer.parseInt(values[1]);
            z = Integer.parseInt(values[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Input must contain 3 comma-separated integers");
        }
        return BlockVector3.at(x, y, z);
    }
}
