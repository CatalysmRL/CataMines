package me.catalysmrl.catamines.mine.reward.trigger;

public interface Trigger {

    String getId();

    Class<? extends TriggerContext> getContextType();

    void fire(TriggerContext context);

}
