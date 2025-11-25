package me.catalysmrl.catamines.mine.abstraction;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.mine.components.manager.choice.ChoiceManager;
import me.catalysmrl.catamines.mine.components.manager.controller.CataMineController;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.mine.components.MineFlags;

public abstract class AbstractCataMine implements CataMine {

    protected final CataMines plugin;

    protected String name;
    protected String displayName;
    protected CataMineController controller;
    protected ChoiceManager<CataMineRegion> regionManager;

    protected MineFlags flags;

    public AbstractCataMine(CataMines plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.displayName = "default";
        controller = new CataMineController(this);
        regionManager = new ChoiceManager<>();
        flags = new MineFlags();
    }

    @Override
    public void tick() {
        if (flags.isStopped()) return;
        controller.tick();
    }

    @Override
    public boolean isStopped() {
        return flags.isStopped();
    }

    @Override
    public void setStopped(boolean stopped) {
        flags.setStopped(stopped);
    }

    @Override
    public org.bukkit.Location getTeleportLocation() {
        return flags.getTeleportLocation();
    }

    @Override
    public void setTeleportLocation(org.bukkit.Location location) {
        flags.setTeleportLocation(location);
    }

    @Override
    public org.bukkit.Location getResetTeleportLocation() {
        return flags.getResetTeleportLocation();
    }

    @Override
    public void setResetTeleportLocation(org.bukkit.Location location) {
        flags.setResetTeleportLocation(location);
    }

    @Override
    public boolean isWarn() {
        return flags.isWarn();
    }

    @Override
    public void setWarn(boolean warn) {
        flags.setWarn(warn);
    }

    @Override
    public boolean isWarnHotbar() {
        return flags.isWarnHotbar();
    }

    @Override
    public void setWarnHotbar(boolean warnHotbar) {
        flags.setWarnHotbar(warnHotbar);
    }

    @Override
    public boolean isWarnGlobal() {
        return flags.isWarnGlobal();
    }

    @Override
    public void setWarnGlobal(boolean warnGlobal) {
        flags.setWarnGlobal(warnGlobal);
    }

    @Override
    public int getWarnSeconds() {
        return flags.getWarnSeconds();
    }

    @Override
    public void setWarnSeconds(int warnSeconds) {
        flags.setWarnSeconds(warnSeconds);
    }

    @Override
    public int getWarnDistance() {
        return flags.getWarnDistance();
    }

    @Override
    public void setWarnDistance(int warnDistance) {
        flags.setWarnDistance(warnDistance);
    }

    @Override
    public boolean isTeleportPlayers() {
        return flags.isTeleportPlayers();
    }

    @Override
    public void setTeleportPlayers(boolean teleportPlayers) {
        flags.setTeleportPlayers(teleportPlayers);
    }

    @Override
    public void reset(CataMines plugin) {
        regionManager.getUpcoming().ifPresent(region -> plugin.getMineManager().resetRegion(region));
        regionManager.next();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        if ("*".equals(name)) throw new IllegalArgumentException();
        this.name = name;
    }

    @Override
    public String getDisplayName() {
        return "default".equals(displayName) ? name : displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public CataMineController getController() {
        return controller;
    }

    @Override
    public void setController(CataMineController controller) {
        this.controller = controller;
    }

    @Override
    public ChoiceManager<CataMineRegion> getRegionManager() {
        return regionManager;
    }

    @Override
    public CataMines getPlugin() {
        return plugin;
    }
}
