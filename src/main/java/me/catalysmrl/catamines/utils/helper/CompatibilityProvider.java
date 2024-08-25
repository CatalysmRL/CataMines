package me.catalysmrl.catamines.utils.helper;

import org.bukkit.Bukkit;

public final class CompatibilityProvider {

    private CompatibilityProvider() {
        //no instance
    }

    private static boolean faweEnabled;
    private static boolean papiEnabled;

    public static void checkCompatibility() {
        faweEnabled = Bukkit.getPluginManager().isPluginEnabled("FastAsyncWorldEdit");
        papiEnabled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public static boolean isFaweEnabled() {
        return faweEnabled;
    }

    public static boolean isPapiEnabled() {
        return papiEnabled;
    }
}
