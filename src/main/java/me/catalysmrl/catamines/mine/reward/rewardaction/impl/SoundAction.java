package me.catalysmrl.catamines.mine.reward.rewardaction.impl;

import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;

import me.catalysmrl.catamines.api.serialization.SectionSerializable;
import me.catalysmrl.catamines.mine.reward.rewardaction.RewardAction;
import me.catalysmrl.catamines.mine.reward.rewardaction.RewardContext;

public class SoundAction implements RewardAction, SectionSerializable {

    private final Sound sound;
    private final float volume;
    private final float pitch;

    public SoundAction(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void execute(RewardContext context) {
        context.getLocation().getWorld().playSound(context.getLocation(), sound, volume, pitch);
    }

    @Override
    public void serialize(ConfigurationSection section) {
        section.set("sound", sound.name());
        section.set("volume", volume);
        section.set("pitch", pitch);
    }

    public static SoundAction deserialize(ConfigurationSection section) {
        Sound sound = Sound.valueOf(section.getString("sound"));
        float volume = (float) section.getDouble("volume");
        float pitch = (float) section.getDouble("pitch");

        return new SoundAction(sound, volume, pitch);
    }

}
