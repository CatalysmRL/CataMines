package me.catalysmrl.catamines.api.mine;

public interface PropertyHolder {

    <T> T getFlag(Flag<T> flag);

    <T> void setFlag(Flag<T> flag, T value);

    boolean hasFlag(Flag<?> flag);

    PropertyHolder getParent();

    default <T> T getFlagRecursive(Flag<T> flag) {
        if (hasFlag(flag)) {
            return getFlag(flag);
        }
        PropertyHolder parent = getParent();
        if (parent != null) {
            return parent.getFlagRecursive(flag);
        }
        return null;
    }
}
