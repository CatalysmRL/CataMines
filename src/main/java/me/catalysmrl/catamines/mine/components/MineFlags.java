package me.catalysmrl.catamines.mine.components;

import me.catalysmrl.catamines.api.mine.Flag;
import me.catalysmrl.catamines.api.serialization.SectionSerializable;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class MineFlags implements SectionSerializable {

    private Boolean stopped;
    private Boolean warn;
    private Boolean warnHotbar;
    private Boolean warnGlobal;
    private Integer warnSeconds;
    private Integer warnDistance;
    private Boolean teleportPlayers;
    private Location teleportLocation;
    private Location resetTeleportLocation;

    public MineFlags() {
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Flag<T> flag) {
        if (flag.getName().equals("stopped"))
            return (T) stopped;
        if (flag.getName().equals("warn"))
            return (T) warn;
        if (flag.getName().equals("warn-hotbar"))
            return (T) warnHotbar;
        if (flag.getName().equals("warn-global"))
            return (T) warnGlobal;
        if (flag.getName().equals("warn-seconds"))
            return (T) warnSeconds;
        if (flag.getName().equals("warn-distance"))
            return (T) warnDistance;
        if (flag.getName().equals("teleport-players"))
            return (T) teleportPlayers;
        if (flag.getName().equals("teleport-location"))
            return (T) teleportLocation;
        if (flag.getName().equals("reset-teleport-location"))
            return (T) resetTeleportLocation;
        return null;
    }

    public <T> void set(Flag<T> flag, T value) {
        if (flag.getName().equals("stopped"))
            this.stopped = (Boolean) value;
        if (flag.getName().equals("warn"))
            this.warn = (Boolean) value;
        if (flag.getName().equals("warn-hotbar"))
            this.warnHotbar = (Boolean) value;
        if (flag.getName().equals("warn-global"))
            this.warnGlobal = (Boolean) value;
        if (flag.getName().equals("warn-seconds"))
            this.warnSeconds = (Integer) value;
        if (flag.getName().equals("warn-distance"))
            this.warnDistance = (Integer) value;
        if (flag.getName().equals("teleport-players"))
            this.teleportPlayers = (Boolean) value;
        if (flag.getName().equals("teleport-location"))
            this.teleportLocation = (Location) value;
        if (flag.getName().equals("reset-teleport-location"))
            this.resetTeleportLocation = (Location) value;
    }

    @Override
    public void serialize(ConfigurationSection section) {
        if (stopped != null)
            section.set("stopped", stopped);
        if (warn != null)
            section.set("warn", warn);
        if (warnHotbar != null)
            section.set("warn-hotbar", warnHotbar);
        if (warnGlobal != null)
            section.set("warn-global", warnGlobal);
        if (warnSeconds != null)
            section.set("warn-seconds", warnSeconds);
        if (warnDistance != null)
            section.set("warn-distance", warnDistance);
        if (teleportPlayers != null)
            section.set("teleport-players", teleportPlayers);
        if (teleportLocation != null)
            section.set("teleport-location", teleportLocation);
        if (resetTeleportLocation != null)
            section.set("reset-teleport-location", resetTeleportLocation);
    }

    public static MineFlags deserialize(ConfigurationSection section) {
        MineFlags flags = new MineFlags();
        if (section == null)
            return flags;

        if (section.contains("stopped"))
            flags.setStopped(section.getBoolean("stopped"));
        if (section.contains("warn"))
            flags.setWarn(section.getBoolean("warn"));
        if (section.contains("warn-hotbar"))
            flags.setWarnHotbar(section.getBoolean("warn-hotbar"));
        if (section.contains("warn-global"))
            flags.setWarnGlobal(section.getBoolean("warn-global"));
        if (section.contains("warn-seconds"))
            flags.setWarnSeconds(section.getInt("warn-seconds"));
        if (section.contains("warn-distance"))
            flags.setWarnDistance(section.getInt("warn-distance"));
        if (section.contains("teleport-players"))
            flags.setTeleportPlayers(section.getBoolean("teleport-players"));
        if (section.contains("teleport-location"))
            flags.setTeleportLocation(section.getLocation("teleport-location"));
        if (section.contains("reset-teleport-location"))
            flags.setResetTeleportLocation(section.getLocation("reset-teleport-location"));

        return flags;
    }

    public boolean isStopped() {
        return stopped != null && stopped;
    }

    public void setStopped(Boolean stopped) {
        this.stopped = stopped;
    }

    public Location getTeleportLocation() {
        return teleportLocation;
    }

    public void setTeleportLocation(Location teleportLocation) {
        this.teleportLocation = teleportLocation;
    }

    public Location getResetTeleportLocation() {
        return resetTeleportLocation;
    }

    public void setResetTeleportLocation(Location resetTeleportLocation) {
        this.resetTeleportLocation = resetTeleportLocation;
    }

    public boolean isWarn() {
        return warn != null && warn;
    }

    public void setWarn(Boolean warn) {
        this.warn = warn;
    }

    public boolean isWarnHotbar() {
        return warnHotbar != null && warnHotbar;
    }

    public void setWarnHotbar(Boolean warnHotbar) {
        this.warnHotbar = warnHotbar;
    }

    public boolean isWarnGlobal() {
        return warnGlobal != null && warnGlobal;
    }

    public void setWarnGlobal(Boolean warnGlobal) {
        this.warnGlobal = warnGlobal;
    }

    public Integer getWarnSeconds() {
        return warnSeconds;
    }

    public void setWarnSeconds(Integer warnSeconds) {
        this.warnSeconds = warnSeconds;
    }

    public Integer getWarnDistance() {
        return warnDistance;
    }

    public void setWarnDistance(Integer warnDistance) {
        this.warnDistance = warnDistance;
    }

    public boolean isTeleportPlayers() {
        return teleportPlayers != null && teleportPlayers;
    }

    public void setTeleportPlayers(Boolean teleportPlayers) {
        this.teleportPlayers = teleportPlayers;
    }
}
