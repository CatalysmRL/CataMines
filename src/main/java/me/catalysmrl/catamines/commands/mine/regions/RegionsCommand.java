package me.catalysmrl.catamines.commands.mine.regions;

import com.google.common.collect.ImmutableList;
import me.catalysmrl.catamines.command.abstraction.mine.AbstractMineCommand;
import me.catalysmrl.catamines.command.abstraction.mine.ParentMineCommand;
import me.catalysmrl.catamines.commands.mine.regions.subcommands.RegionCreateCommand;
import me.catalysmrl.catamines.commands.mine.regions.subcommands.RegionDeleteCommand;
import me.catalysmrl.catamines.commands.mine.regions.subcommands.RegionResetCommand;

import me.catalysmrl.catamines.utils.message.Message;

public class RegionsCommand extends ParentMineCommand {

    public RegionsCommand() {
        super("regions", ImmutableList.<AbstractMineCommand>builder()
                .add(new RegionCreateCommand())
                .add(new RegionDeleteCommand())
                .add(new RegionResetCommand())
                .build());
    }

    @Override
    public Message getDescription() {
        return Message.REGIONS_DESCRIPTION;
    }

    @Override
    public Message getUsage() {
        return Message.REGIONS_USAGE;
    }
}
