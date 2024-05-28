package me.catalysmrl.catamines.utils.worldedit;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.session.ClipboardHolder;
import me.catalysmrl.catamines.CataMines;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WorldEditUtils {

    private WorldEditUtils() {
    }

    public static RegionSelector getSelector(Player player) {
        return WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(player)).getRegionSelector(BukkitAdapter.adapt(player.getWorld()));
    }

    public static void pasteRegion(Region region, Pattern pattern) {
        try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
                .world(region.getWorld())
                .changeSetNull()
                .allowedRegionsEverywhere()
                .fastMode(true)
                .build()) {

            editSession.setReorderMode(EditSession.ReorderMode.FAST);
            editSession.setBlocks(region, pattern);
        } catch (MaxChangedBlocksException exception) {
            exception.printStackTrace();
        }
    }

    public static void pasteSchematic(Clipboard clipboard, BlockVector3 location) {
        if (clipboard == null)
            throw new IllegalArgumentException("Invalid Clipboard");

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(clipboard.getRegion().getWorld())) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(location)
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
}
