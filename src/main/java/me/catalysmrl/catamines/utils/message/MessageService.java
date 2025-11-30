package me.catalysmrl.catamines.utils.message;

import org.bukkit.command.CommandSender;

import java.util.List;
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

    public static void sendList(CommandSender sender, String key, Object... args) {
        formatList(sender, key, args).forEach(sender::sendMessage);
    }

    public static void sendPrefixed(CommandSender sender, String key, Object... args) {
        sender.sendMessage(Messages.prefix(format(sender, key, args)));
    }

    public static String format(CommandSender sender, String key, Object... args) {
        Locale locale = localeManager.getLocale(sender);
        return localeManager.getFormatted(locale, key, args);
    }

    public static List<String> formatList(CommandSender sender, String key, Object... args) {
        Locale locale = localeManager.getLocale(sender);
        return localeManager.getFormattedList(locale, key, args);
    }

    public static LocaleManager getLocaleManager() {
        return localeManager;
    }
}
