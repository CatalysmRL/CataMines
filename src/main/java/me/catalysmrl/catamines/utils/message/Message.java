package me.catalysmrl.catamines.utils.message;

import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Locale;

public class Message {

    public static final Message UNKNOWN_COMMAND = Message.of("command.unknown-command");
    public static final Message ONLY_PLAYERS = Message.of("command.only-players");
    public static final Message NO_PERMISSION = Message.of("command.no-permission");

    private final String key;

    private Message(String key) {
        this.key = key;
    }

    public static Message of(String key) {
        return new Message(key);
    }

    public void send(CommandSender sender, Object... args) {
        MessageService.send(sender, key, args);
    }

    public void sendPrefixed(CommandSender sender, Object... args) {
        MessageService.sendPrefixed(sender, key, args);
    }

    public String format(CommandSender sender, Object... args) {
        return MessageService.format(sender, key, args);
    }

    public List<String> formatList(CommandSender sender, Object... args) {
        Locale locale = MessageService.getLocaleManager().getLocale(sender);
        return MessageService.getLocaleManager().getFormattedList(locale, key, args);
    }

    public String getKey() {
        return key;
    }
}
