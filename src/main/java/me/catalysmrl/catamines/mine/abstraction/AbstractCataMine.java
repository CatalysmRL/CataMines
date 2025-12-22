package me.catalysmrl.catamines.mine.abstraction;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.mine.components.manager.choice.ChoiceManager;
import me.catalysmrl.catamines.mine.components.manager.controller.CataMineController;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;
import me.catalysmrl.catamines.mine.reward.RewardContainer;
import me.catalysmrl.catamines.mine.components.MineFlags;

public abstract class AbstractCataMine implements CataMine, Cloneable {

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

    @Override
    public boolean hasRewardsFor(String triggerId) {
        return false;
    }

    @Override
    public RewardContainer getRewardsFor(String triggerId) {
        return null;
    }

    @Override
    public CataMine clone() {
        try {
            AbstractCataMine clone = (AbstractCataMine) super.clone();

            clone.flags = this.flags.clone();

            // Manually clone controller to ensure the new mine instance is used
            clone.controller = new CataMineController(clone);
            clone.controller.setResetMode(this.controller.getResetMode());
            clone.controller.setResetDelay(this.controller.getResetDelay());
            clone.controller.setResetPercentage(this.controller.getResetPercentage());
            clone.controller.setCountdown(this.controller.getCountdown());
            clone.controller.setBlockCount(this.controller.getBlockCount());

            clone.regionManager = new ChoiceManager<>();
            for (CataMineRegion region : this.regionManager.getChoices()) {
                CataMineRegion clonedRegion = region.clone();
                clonedRegion.setMine(clone);
                clone.regionManager.add(clonedRegion);

                if (this.regionManager.getCurrent().isPresent() &&
                        this.regionManager.getCurrent().get().getName().equals(region.getName())) {
                    clone.regionManager.setCurrent(clonedRegion);
                }
                if (this.regionManager.getUpcoming().isPresent() &&
                        this.regionManager.getUpcoming().get().getName().equals(region.getName())) {
                    clone.regionManager.setUpcoming(clonedRegion);
                }
            }

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
