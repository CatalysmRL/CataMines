package me.catalysmrl.catamines.utils.helper;

import com.google.common.collect.Range;

import java.util.function.Predicate;

public final class Predicates {

    private Predicates() {}

    public static Predicate<Integer> inRange(int start, int end) {
        Range<Integer> range = Range.closed(start, end);
        return range::contains;
    }

}
