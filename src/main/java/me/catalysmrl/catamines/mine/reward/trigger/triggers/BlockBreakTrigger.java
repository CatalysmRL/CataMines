package me.catalysmrl.catamines.mine.reward.trigger.triggers;

import me.catalysmrl.catamines.mine.reward.trigger.Trigger;
import me.catalysmrl.catamines.mine.reward.trigger.TriggerContext;
import me.catalysmrl.catamines.mine.reward.trigger.context.BlockBreakContext;

public class BlockBreakTrigger implements Trigger {

    @Override
    public String getId() {
        return "block-break";
    }

    @Override
    public Class<? extends TriggerContext> getContextType() {
        return BlockBreakContext.class;
    }

    @Override
    public void fire(TriggerContext context) {
        BlockBreakContext blockBreakContext = (BlockBreakContext) context;

        blockBreakContext.player().sendMessage("Block break trigger");
    }
}
