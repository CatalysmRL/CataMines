package me.catalysmrl.catamines.utils.metrics;

import me.catalysmrl.catamines.utils.message.Messages;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

public class UpdateChecker implements Listener {

    private final int RESOURCE_ID = 96457;
    private final JavaPlugin plugin;
    private final String pluginVersion;
    private String spigotVersion;
    private boolean updateAvailable;

    public UpdateChecker(JavaPlugin plugin) {
        this.plugin = plugin;
        this.pluginVersion = plugin.getDescription().getVersion();
    }

    public void fetch() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (InputStream is = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + RESOURCE_ID).openStream();
                 Scanner scanner = new Scanner(is)) {

                if (scanner.hasNext()) {
                    spigotVersion = scanner.next();
                }

            } catch (IOException exception) {
                plugin.getLogger().warning("Failed to fetch for updates on spigot.");
                return;
            }

            if (spigotVersion == null || spigotVersion.isEmpty()) {
                return;
            }

            updateAvailable = spigotIsNewer();

            if (!updateAvailable) {
                return;
            }

            Bukkit.getScheduler().runTask(plugin, () -> {
                plugin.getLogger().info("A new version of CataMines is available (v" + spigotVersion + ") at:");
                plugin.getLogger().info("https://www.spigotmc.org/resources/96457/");
                Bukkit.getPluginManager().registerEvents(this, plugin);
            });
        });
    }

    private boolean spigotIsNewer() {
        if (spigotVersion == null || spigotVersion.isEmpty()) {
            return false;
        }

        int[] plV = toReadable(pluginVersion);
        int[] spV = toReadable(spigotVersion);

        if (plV[0] < spV[0]) {
            return true;
        } else if ((plV[1] < spV[1])) {
            return true;
        } else {
            return plV[2] < spV[2];
        }
    }

    private int[] toReadable(String version) {
        return Arrays.stream(version.split("\\.")).mapToInt(Integer::parseInt).toArray();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().isOp()) {
            Messages.sendPrefixed(event.getPlayer(),
                    "An update for &bCata&aMines &6(v" + spigotVersion + ")",
                    "&7is available at: &dhttps://spigotmc.org/resources/96457/");
        }
    }

}
