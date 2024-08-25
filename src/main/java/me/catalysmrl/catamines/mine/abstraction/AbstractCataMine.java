package me.catalysmrl.catamines.mine.abstraction;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.mine.components.manager.choice.ChoiceManager;
import me.catalysmrl.catamines.mine.components.manager.controller.CataMineController;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;

public abstract class AbstractCataMine implements CataMine {

    private final CataMines plugin;

    protected String name;
    protected String displayName;
    protected CataMineController controller;
    protected ChoiceManager<CataMineRegion> regionManager;

    public AbstractCataMine(CataMines plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.displayName = "default";
        controller = new CataMineController(this);
        regionManager = new ChoiceManager<>();
    }

    @Override
    public void tick() {
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
