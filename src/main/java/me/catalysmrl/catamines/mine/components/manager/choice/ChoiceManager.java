package me.catalysmrl.catamines.mine.components.manager.choice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class ChoiceManager<T extends Identifiable & Choice> {

    private T current;
    private T upcoming;

    private final List<T> choices = new ArrayList<>();
    private double max = 0;

    public ChoiceManager() {
    }

    public ChoiceManager(List<T> choices) {
        this.choices.addAll(choices);
        next();
    }

    public void next() {
        current = upcoming;

        double randomValue = ThreadLocalRandom.current().nextDouble() * max;
        double offset = 0;

        for (T choice : choices) {
            offset += choice.getChance();
            if (randomValue < offset) {
                upcoming = choice;
                return;
            }
        }
    }

    public void add(T choice) {
        choices.add(choice);
        max += choice.getChance();
        if (upcoming == null)
            upcoming = choice;
    }

    public void remove(T choice) {
        choices.remove(choice);
    }

    public Optional<T> get(String name) {
        return getChoices().stream().filter(choice -> choice.getName().equals(name)).findFirst();
    }

    public Optional<T> getCurrent() {
        return Optional.ofNullable(current);
    }

    public void setCurrent(T current) {
        this.current = current;
    }

    public Optional<T> getUpcoming() {
        return Optional.ofNullable(upcoming);
    }

    public void setUpcoming(T upcoming) {
        this.upcoming = upcoming;
    }

    public List<T> getChoices() {
        return choices;
    }

    @Override
    public String toString() {
        return "ChoiceManager{" +
                "current=" + current +
                ", upcoming=" + upcoming +
                ", choices=" + choices +
                '}';
    }
}
