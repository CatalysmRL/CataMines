package me.catalysmrl.catamines.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.catalysmrl.catamines.utils.message.Message;

public final class ConfirmationManager {
    private static final Map<UUID, Pair<Runnable, Long>> pending = new HashMap<>();
    private static final UUID CONSOLE_UUID = new UUID(0, 0);

    public static void request(CommandSender sender, int affected, Runnable action) {
        UUID uuid = getUUID(sender);
        pending.put(uuid, Pair.of(action, System.currentTimeMillis() + 30_000));
        Message.CONFIRM_HEADER.send(sender, affected);
        Message.CONFIRM_FOOTER.send(sender);
    }

    public static boolean confirm(CommandSender sender) {
        UUID uuid = getUUID(sender);
        var entry = pending.get(uuid);
        
        if (entry == null) return false;
        
        if (System.currentTimeMillis() > entry.getRight()) {
            pending.remove(uuid);
            return false;
        }
        
        pending.remove(uuid);
        entry.getLeft().run();
        return true;
    }
    
    public static boolean hasPendingRequest(CommandSender sender) {
        UUID uuid = getUUID(sender);
        var entry = pending.get(uuid);
        if (entry == null) return false;
        if (System.currentTimeMillis() > entry.getRight()) {
            pending.remove(uuid);
            return false;
        }
        return true;
    }

    private static UUID getUUID(CommandSender sender) {
        if (sender instanceof Player p) {
            return p.getUniqueId();
        }
        return CONSOLE_UUID;
    }
}
