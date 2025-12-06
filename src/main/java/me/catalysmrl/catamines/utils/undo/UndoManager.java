package me.catalysmrl.catamines.utils.undo;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

public final class UndoManager {

    private static final Map<UUID, Deque<UndoEntry>> undoHistory = new HashMap<>();
    private static final Map<UUID, Deque<UndoEntry>> redoHistory = new HashMap<>();
    private static final int MAX_HISTORY = 10;
    private static final UUID CONSOLE_UUID = new UUID(0, 0);

    private record UndoEntry(
            long timestamp,
            Map<String, CataMine> snapshots,
            String description) {
    }

    public record Result(
            int restored,
            String description) {
    }

    private static UUID getUUID(Object sender) {
        if (sender instanceof Player player) {
            return player.getUniqueId();
        }
        return CONSOLE_UUID;
    }

    public static void record(CommandSender sender, Collection<CataMine> affectedMines, String description) {
        if (affectedMines.isEmpty())
            return;

        Map<String, CataMine> snapshots = new HashMap<>();
        for (CataMine mine : affectedMines) {
            snapshots.put(mine.getName(), mine.clone());
        }

        addEntry(sender, new UndoEntry(System.currentTimeMillis(), snapshots, description));
    }

    public static void recordCreation(CommandSender sender, String mineName) {
        Map<String, CataMine> snapshots = new HashMap<>();
        snapshots.put(mineName, null);
        addEntry(sender, new UndoEntry(System.currentTimeMillis(), snapshots, "create " + mineName));
    }

    private static void addEntry(CommandSender sender, UndoEntry entry) {
        Deque<UndoEntry> queue = undoHistory.computeIfAbsent(getUUID(sender), k -> new ArrayDeque<>());
        queue.addLast(entry);
        while (queue.size() > MAX_HISTORY)
            queue.removeFirst();

        redoHistory.remove(getUUID(sender));
    }

    public static Result undoLast(CommandSender sender, CataMines plugin) {
        Deque<UndoEntry> queue = undoHistory.get(getUUID(sender));
        if (queue == null || queue.isEmpty()) {
            return null;
        }

        UndoEntry entry = queue.removeLast();

        Map<String, CataMine> current = new HashMap<>();
        for (String name : entry.snapshots().keySet()) {
            Optional<CataMine> mOpt = plugin.getMineManager().getMine(name);
            current.put(name, mOpt.map(CataMine::clone).orElse(null));
        }

        redoHistory.computeIfAbsent(getUUID(sender), k -> new ArrayDeque<>())
                .addLast(new UndoEntry(System.currentTimeMillis(), current, entry.description));

        int restored = restoreSnapshots(entry, plugin);
        return new Result(restored, entry.description);
    }

    public static Result redoLast(CommandSender sender, CataMines plugin) {
        Deque<UndoEntry> queue = redoHistory.get(getUUID(sender));
        if (queue == null || queue.isEmpty()) {
            return null;
        }

        UndoEntry entry = queue.removeLast();

        Map<String, CataMine> current = new HashMap<>();
        for (String name : entry.snapshots().keySet()) {
            Optional<CataMine> mOpt = plugin.getMineManager().getMine(name);
            current.put(name, mOpt.map(CataMine::clone).orElse(null));
        }

        undoHistory.computeIfAbsent(getUUID(sender), k -> new ArrayDeque<>())
                .addLast(new UndoEntry(System.currentTimeMillis(), current, entry.description));

        int restored = restoreSnapshots(entry, plugin);

        return new Result(restored, entry.description);
    }

    private static int restoreSnapshots(UndoEntry entry, CataMines plugin) {
        int restored = 0;

        for (Map.Entry<String, CataMine> e : entry.snapshots.entrySet()) {
            String name = e.getKey();
            CataMine oldMine = e.getValue();

            List<CataMine> mineList = plugin.getMineManager().getMines();

            if (oldMine == null) {
                // DELETE MINE
                Optional<CataMine> existing = plugin.getMineManager().getMine(name);
                if (existing.isPresent()) {
                    try {
                        plugin.getMineManager().deleteMine(existing.get());
                        restored++;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                // RESTORE MINE
                mineList.removeIf(m -> m.getName().equals(name));
                mineList.add(oldMine);

                try {
                    plugin.getMineManager().saveMine(oldMine);
                    restored++;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return restored;
    }

    public static void clear(CommandSender sender) {
        undoHistory.remove(getUUID(sender));
        redoHistory.remove(getUUID(sender));
    }
}