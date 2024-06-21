package me.catalysmrl.catamines.mine.components.manager.controller;

import me.catalysmrl.catamines.api.mine.CataMine;
import me.catalysmrl.catamines.api.serialization.SectionSerializable;
import org.bukkit.configuration.ConfigurationSection;

public class CataMineController implements SectionSerializable {

    private final CataMine mine;

    private ResetMode resetMode = ResetMode.TIME;
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

        countdown--;
        return false;
    }

    public boolean tickPercentage() {
        // Implement logic for PERCENTAGE mode
        return false;
    }

    @Override
    public void serialize(ConfigurationSection section) {
        section.set("reset-mode", resetMode.toString());
        section.set("reset-delay", resetDelay);
        section.set("reset-percentage", resetPercentage);

        section.set("countdown", countdown);
    }

    public static CataMineController deserialize(ConfigurationSection section, CataMine mine) {
        ResetMode mode = ResetMode.valueOf(section.getString("reset-mode"));
        int resetDelay = section.getInt("reset-delay", -0);
        double resetPercentage = section.getDouble("reset-percentage", 0d);

        int countdown = section.getInt("countdown", 0);

        CataMineController controller = new CataMineController(mine);
        controller.setResetMode(mode);
        controller.setResetDelay(resetDelay);
        controller.setResetPercentage(resetPercentage);
        controller.setCountdown(countdown);
        return controller;
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
