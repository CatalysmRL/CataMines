package me.catalysmrl.catamines.utils.helper;

import java.util.function.Predicate;

public final class Predicates {

    private Predicates() {
    }

    public static Predicate<Integer> equals(int value) {
        return i -> i == value;
    }

    public static Predicate<Integer> atLeast(int min) {
        return i -> i >= min;
    }

    public static Predicate<Integer> inRange(int min, int max) {
        return i -> i >= min && i <= max;
    }

    public static Predicate<Integer> any() {
        return i -> true;
    }
}
