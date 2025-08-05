package me.catalysmrl.catamines.command.abstraction;

import java.util.ArrayList;
import java.util.List;

public class CommandContext {
    private final List<String> args;
    private int index = 0;

    public CommandContext(List<String> args) {
        this.args = args;
    }

    public CommandContext(List<String> args, int index) {
        this.args = args;
        this.index = index;
    }

    public boolean hasNext() {
        return index < args.size();
    }

    public String next() {
        return hasNext() ? args.get(index++) : null;
    }

    public String peek() {
        return hasNext() ? args.get(index) : null;
    }

    public int remaining() {
        return args.size() - index;
    }

    public List<String> getRemainingArgs() {
        return args.subList(index, args.size());
    }

    public CommandContext copy() {
        return new CommandContext(new ArrayList<>(args), index);
    }
}
