package me.catalysmrl.catamines.commands.mine.regions;

import me.catalysmrl.catamines.command.abstraction.AbstractCataMineCommand;
import me.catalysmrl.catamines.command.abstraction.ParentMineCommand;

import java.util.List;

public class RegionsCommand extends ParentMineCommand {

    public RegionsCommand(String name, List<AbstractCataMineCommand> children) {
        super(name, children);
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }

}
