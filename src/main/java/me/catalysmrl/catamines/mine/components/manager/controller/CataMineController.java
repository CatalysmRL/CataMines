package me.catalysmrl.catamines.mine.components.manager.controller;

import me.catalysmrl.catamines.api.mine.CataMine;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CataMineController implements ConfigurationSerializable {

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
        if (resetDelay < 0) return true;

        if (countdown <= 0) {
            countdown = resetDelay;
            mine.reset();
            return true;
        }


        return false;
    }

    public boolean tickPercentage() {
        // Implement logic for PERCENTAGE mode
        return false;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        return Map.of();
    }

    public static CataMineController deserialize(Map<String, Object> serializedController) {


        return null;
    }

    public ResetMode getResetMode() {
        return resetMode;
    }

    public void setResetMode(ResetMode resetMode) {
        this.resetMode = resetMode;
    }

    public int getResetDelay() {
        return resetDelay;
    }

    public void setResetDelay(int resetDelay) {
        this.resetDelay = resetDelay;
    }

    public double getResetPercentage() {
        return resetPercentage;
    }

    public void setResetPercentage(double resetPercentage) {
        this.resetPercentage = resetPercentage;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public long getBlockCount() {
        return blockCount;
    }

    public void setBlockCount(long blockCount) {
        this.blockCount = blockCount;
    }

    public enum ResetMode {
        TIME,
        PERCENTAGE,
        TIME_PERCENTAGE
    }
}
