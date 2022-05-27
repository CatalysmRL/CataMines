package de.c4t4lysm.catamines.listeners;

import de.c4t4lysm.catamines.CataMines;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("catamines.*") || !CataMines.getInstance().updateAvailable || !CataMines.getInstance().getConfig().getBoolean("announceUpdate", true)) {
            return;
        }
        player.sendMessage(CataMines.PREFIX + "An update for CataMines is available.\n" +
                "Download it from: §ahttps://spigotmc.org/resources/96457/");
    }

}
