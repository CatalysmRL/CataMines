package me.catalysmrl.catamines.mine.components;

import me.catalysmrl.catamines.api.serialization.SectionSerializable;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class MineFlags implements SectionSerializable {

    private boolean stopped;
    private Location teleportLocation;
    private Location resetTeleportLocation;
    private boolean warn;
    private boolean warnHotbar;
    private boolean warnGlobal;
    private int warnSeconds = 10;
    private int warnDistance = 10;
    private boolean teleportPlayers;

    public MineFlags() {
    }

    @Override
    public void serialize(ConfigurationSection section) {
        section.set("stopped", stopped);
        section.set("teleport-location", teleportLocation);
        section.set("reset-teleport-location", resetTeleportLocation);
        section.set("warn", warn);
        section.set("warn-hotbar", warnHotbar);
        section.set("warn-global", warnGlobal);
        section.set("warn-seconds", warnSeconds);
        section.set("warn-distance", warnDistance);
        section.set("teleport-players", teleportPlayers);
    }

    public static MineFlags deserialize(ConfigurationSection section) {
        MineFlags flags = new MineFlags();
        if (section == null) return flags;

        flags.setStopped(section.getBoolean("stopped"));
        flags.setTeleportLocation(section.getLocation("teleport-location"));
        flags.setResetTeleportLocation(section.getLocation("reset-teleport-location"));
        flags.setWarn(section.getBoolean("warn"));
        flags.setWarnHotbar(section.getBoolean("warn-hotbar"));
        flags.setWarnGlobal(section.getBoolean("warn-global"));
        flags.setWarnSeconds(section.getInt("warn-seconds", 10));
        flags.setWarnDistance(section.getInt("warn-distance", 10));
        flags.setTeleportPlayers(section.getBoolean("teleport-players"));

        return flags;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
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
        return warn;
    }

    public void setWarn(boolean warn) {
        this.warn = warn;
    }

    public boolean isWarnHotbar() {
        return warnHotbar;
    }

    public void setWarnHotbar(boolean warnHotbar) {
        this.warnHotbar = warnHotbar;
    }

    public boolean isWarnGlobal() {
        return warnGlobal;
    }

    public void setWarnGlobal(boolean warnGlobal) {
        this.warnGlobal = warnGlobal;
    }

    public int getWarnSeconds() {
        return warnSeconds;
    }

    public void setWarnSeconds(int warnSeconds) {
        this.warnSeconds = warnSeconds;
    }

    public int getWarnDistance() {
        return warnDistance;
    }

    public void setWarnDistance(int warnDistance) {
        this.warnDistance = warnDistance;
    }

    public boolean isTeleportPlayers() {
        return teleportPlayers;
    }

    public void setTeleportPlayers(boolean teleportPlayers) {
        this.teleportPlayers = teleportPlayers;
    }
}
