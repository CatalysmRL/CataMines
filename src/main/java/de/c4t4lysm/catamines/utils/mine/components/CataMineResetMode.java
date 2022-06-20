package de.c4t4lysm.catamines.utils.mine.components;

public enum CataMineResetMode {
    TIME,
    PERCENTAGE,
    TIME_PERCENTAGE;

    private final static CataMineResetMode[] resetModes = values();

    public CataMineResetMode next() {
        return resetModes[(this.ordinal() + 1) % resetModes.length];
    }
}
