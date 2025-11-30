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
        if (flags.isStopped())
            return;
        controller.tick();
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
        if ("*".equals(name))
            throw new IllegalArgumentException();
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
    public MineFlags getFlags() {
        return flags;
    }

    @Override
    public CataMines getPlugin() {
        return plugin;
    }

    @Override
    public <T> T getFlag(me.catalysmrl.catamines.api.mine.Flag<T> flag) {
        return flags.get(flag);
    }

    @Override
    public <T> void setFlag(me.catalysmrl.catamines.api.mine.Flag<T> flag, T value) {
        flags.set(flag, value);
    }

    @Override
    public boolean hasFlag(me.catalysmrl.catamines.api.mine.Flag<?> flag) {
        return flags.get(flag) != null;
    }

    @Override
    public me.catalysmrl.catamines.api.mine.PropertyHolder getParent() {
        return null;
    }
}
