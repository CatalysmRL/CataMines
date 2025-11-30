package me.catalysmrl.catamines.api.mine;

import java.util.Objects;

import org.bukkit.Location;

public class Flag<T> {

    public static final Flag<Boolean> STOPPED = new Flag<>("stopped", Boolean.class);
    public static final Flag<Location> TELEPORT_LOCATION = new Flag<>("teleport-location", Location.class);
    public static final Flag<Location> RESET_TELEPORT_LOCATION = new Flag<>("reset-teleport-location", Location.class);
    public static final Flag<Boolean> WARN = new Flag<>("warn", Boolean.class);
    public static final Flag<Boolean> WARN_HOTBAR = new Flag<>("warn-hotbar", Boolean.class);
    public static final Flag<Boolean> WARN_GLOBAL = new Flag<>("warn-global", Boolean.class);
    public static final Flag<Integer> WARN_SECONDS = new Flag<>("warn-seconds", Integer.class);
    public static final Flag<Integer> WARN_DISTANCE = new Flag<>("warn-distance", Integer.class);
    public static final Flag<Boolean> TELEPORT_PLAYERS = new Flag<>("teleport-players", Boolean.class);

    private final String name;
    private final Class<T> type;

    public Flag(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Flag<?> flag = (Flag<?>) o;
        return Objects.equals(name, flag.name);
    }

    public static Flag<?> getByName(String name) {
        if (name.equalsIgnoreCase("stopped"))
            return STOPPED;
        if (name.equalsIgnoreCase("teleport-location"))
            return TELEPORT_LOCATION;
        if (name.equalsIgnoreCase("reset-teleport-location"))
            return RESET_TELEPORT_LOCATION;
        if (name.equalsIgnoreCase("warn"))
            return WARN;
        if (name.equalsIgnoreCase("warn-hotbar"))
            return WARN_HOTBAR;
        if (name.equalsIgnoreCase("warn-global"))
            return WARN_GLOBAL;
        if (name.equalsIgnoreCase("warn-seconds"))
            return WARN_SECONDS;
        if (name.equalsIgnoreCase("warn-distance"))
            return WARN_DISTANCE;
        if (name.equalsIgnoreCase("teleport-players"))
            return TELEPORT_PLAYERS;
        return null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
