package me.catalysmrl.catamines.mine.components.manager.choice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChoiceManager<T extends Identifiable> {

    private T currentChoice;
    private T upcomingChoice;

    private final List<T> choices = new ArrayList<>();

    public void next() {
        currentChoice = upcomingChoice;
    }

    public void add(T choice) {
        choices.add(choice);
    }

    public void remove(T choice) {
        choices.remove(choice);
    }

    public Optional<T> getChoice(String name) {
        return getChoices().stream().filter(region -> region.getName().equals(name)).findFirst();
    }

    public T getCurrentChoice() {
        return currentChoice;
    }

    public T getUpcomingChoice() {
        return upcomingChoice;
    }

    public List<T> getChoices() {
        return choices;
    }
}
