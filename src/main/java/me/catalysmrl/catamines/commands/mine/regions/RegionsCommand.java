package me.catalysmrl.catamines.commands.mine.regions;

import com.google.common.collect.ImmutableList;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.command.abstraction.mine.ParentMineCommand;
import me.catalysmrl.catamines.commands.mine.regions.subcommands.RegionCreateCommand;
import me.catalysmrl.catamines.commands.mine.regions.subcommands.RegionDeleteCommand;

public class RegionsCommand extends ParentMineCommand {

    public RegionsCommand() {
        super("regions", ImmutableList.<AbstractMineCommand>builder()
                .add(new RegionCreateCommand())
                .add(new RegionDeleteCommand())
                .build());
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getUsage() {
        return "/cm regions <mine>";
    }

}
