package me.catalysmrl.catamines.mine.components.manager.controller;

import me.catalysmrl.catamines.api.mine.CataMine;

public class CataMineController {

    private final CataMine mine;

    private ResetMode resetMode;
    private int resetDelay;
    private double resetPercentage;

    private int countdown;
    private long blockCount;

    public CataMineController(CataMine mine) {
        this.mine = mine;
    }

    public boolean tick() {
        switch (resetMode) {
            case TIME -> tickTime();
            case PERCENTAGE -> tickPercentage();
            case TIME_PERCENTAGE -> {
                return tickTime() || tickPercentage();
            }
        }
        return false;
    }

    public boolean tickTime() {
        if (countdown <= 0) {
            countdown = resetDelay;
            return true;
        }
        return false;
    }

    public boolean tickPercentage() {
        // Implement logic for PERCENTAGE mode
        return false;
    }

    public enum ResetMode {
        TIME,
        PERCENTAGE,
        TIME_PERCENTAGE
    }
}
