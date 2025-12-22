package me.catalysmrl.catamines.mine.reward.trigger;

import java.util.HashMap;
import java.util.Map;

public final class TriggerRegistry {

    private static final Map<String, Trigger> TRIGGERS = new HashMap<>();

    public static void register(Trigger trigger) {
        TRIGGERS.put(trigger.getId().toLowerCase(), trigger);
    }

    public static Trigger get(String id) {
        return TRIGGERS.get(id.toLowerCase());
    }

    public static void fire(String triggerId, TriggerContext context) {
        Trigger trigger = get(triggerId);
        if (trigger == null)
            return;

        if (!trigger.getContextType().isAssignableFrom(context.getClass())) {
            throw new IllegalArgumentException("Trigger " + triggerId + " expected "
                    + trigger.getContextType().getSimpleName() + " but got " + context.getClass().getSimpleName());
        }

        trigger.fire(context);
    }

}
