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
import com.sk89q.worldedit.world.World;
import me.catalysmrl.catamines.CataMines;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class WorldEditUtils {

    private WorldEditUtils() {
    }

    public static RegionSelector getSelector(Player player) {
        return WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(player))
                .getRegionSelector(BukkitAdapter.adapt(player.getWorld()));
    }

    public static void pasteRegion(Region region, Pattern pattern) {
        Objects.requireNonNull(region);
        Objects.requireNonNull(pattern);

        try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
                .world(region.getWorld())
                .build()) {

            editSession.setBlocks(region, pattern);
        } catch (MaxChangedBlocksException exception) {
            exception.printStackTrace();
        }
    }

    // Suppressing resource leak warning: Operations.complete() handles resource
    // management internally
    @SuppressWarnings("resource")
    public static void pasteSchematic(Clipboard clipboard, World world, BlockVector3 location) {
        Objects.requireNonNull(clipboard);
        Objects.requireNonNull(world);
        Objects.requireNonNull(location);

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
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
