package me.catalysmrl.catamines.utils.message;

import org.bukkit.command.CommandSender;

import java.util.Locale;

public class MessageService {
    private static LocaleManager localeManager;

    private MessageService() {
    }

    public static void init(LocaleManager manager) {
        localeManager = manager;
    }

    public static void send(CommandSender sender, String key, Object... args) {
        sender.sendMessage(format(sender, key, args));
    }

    public static void sendPrefixed(CommandSender sender, String key, Object... args) {
        sender.sendMessage(Messages.prefix(format(sender, key, args)));
    }

    public static String format(CommandSender sender, String key, Object... args) {
        Locale locale = localeManager.getLocale(sender);
        return localeManager.getFormatted(locale, key, args);
    }

    public static LocaleManager getLocaleManager() {
        return localeManager;
    }
}
