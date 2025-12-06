package me.catalysmrl.catamines.command.utils;

import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.api.mine.Targetable;
import me.catalysmrl.catamines.mine.components.composition.CataMineComposition;
import me.catalysmrl.catamines.mine.components.region.CataMineRegion;

import java.util.Optional;

public class MineTarget {

    private CataMine mine;
    private CataMineRegion region;
    private CataMineComposition composition;

    public MineTarget(CataMine mine, CataMineRegion region, CataMineComposition composition) {
        this.mine = mine;
        this.region = region;
        this.composition = composition;
    }

    public CataMine getMine() {
        return mine;
    }

    public void setMine(CataMine mine) {
        this.mine = mine;
    }

    public CataMineRegion getRegion() {
        return region;
    }

    public void setRegion(CataMineRegion region) {
        this.region = region;
    }

    public CataMineComposition getComposition() {
        return composition;
    }

    public void setComposition(CataMineComposition composition) {
        this.composition = composition;
    }

    public Targetable getTarget() {
        if (composition != null)
            return composition;
        if (region != null)
            return region;
        return mine;
    }

    public String toPath() {
        if (composition != null)
            return mine.getName() + ":" + region.getName() + ":" + composition.getName();
        if (region != null)
            return mine.getName() + ":" + region.getName();
        return mine.getName();
    }

    public static MineTarget resolve(CataMine mine, String path) {
        if (path == null || path.isEmpty()) {
            return new MineTarget(mine, null, null);
        }

        String[] parts = path.split(":");
        CataMineRegion region = null;
        CataMineComposition composition = null;

        if (parts.length > 0) {
            Optional<CataMineRegion> regionOpt = mine.getRegionManager().get(parts[0]);
            if (regionOpt.isPresent()) {
                region = regionOpt.get();
                if (parts.length > 1) {
                    Optional<CataMineComposition> compOpt = region.getCompositionManager().get(parts[1]);
                    if (compOpt.isPresent()) {
                        composition = compOpt.get();
                    }
                }
            }
        }

        return new MineTarget(mine, region, composition);
    }

    public void resolveDefaults() {
        if (region == null) {
            Optional<CataMineRegion> regionOpt = mine.getRegionManager().get("default");
            if (regionOpt.isPresent()) {
                region = regionOpt.get();
                if (composition == null) {
                    Optional<CataMineComposition> compOpt = region.getCompositionManager().get("default");
                    if (compOpt.isPresent()) {
                        composition = compOpt.get();
                    }
                }
            }
        } else if (composition == null) {
            Optional<CataMineComposition> compOpt = region.getCompositionManager().get("default");
            if (compOpt.isPresent()) {
                composition = compOpt.get();
            }
        }
    }
}
