package me.catalysmrl.catamines.utils.message;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class used for message handling such as colorizing or sending
 */
public class Messages {

    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("&#([0-9a-fA-F]{6})");

    /**
     * Plugin prefix
     */
    public static String PREFIX = "&6[&bCata&aMines&6]&r";

    public static String prefix(String message) {
        return colorize(PREFIX + " &7" + message);
    }

    /**
     * Sends a colorized message to the sender
     *
     * @param sender  the sender receiving the message
     * @param message the message to colorize
     */
    public static void sendColorized(CommandSender sender, String message) {
        sender.sendMessage(colorize(message));
    }

    /**
     * Sends a colorized message with the plugin prefix to the sender
     *
     * @param sender  the sender receiving the message
     * @param message the message to prefix and colorize
     */
    public static void sendPrefixed(CommandSender sender, String message) {
        sender.sendMessage(prefix(message));
    }

    public static void sendPrefixed(CommandSender sender, String... messages) {
        if (messages.length == 0) {
            return;
        }

        sendPrefixed(sender, String.join("\n", messages));
    }

    /**
     * Translates color codes in the given input string.
     *
     * @param string the string to "colorize"
     * @return the colorized string
     */
    public static String colorize(String string) {
        if (string == null) {
            return "null";
        }

        // Convert from the '&#rrggbb' hex color format to the '&x&r&r&g&g&b&b' one used
        // by Bukkit.
        Matcher matcher = HEX_COLOR_PATTERN.matcher(string);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            StringBuilder replacement = new StringBuilder(14).append("&x");
            for (char character : matcher.group(1).toCharArray()) {
                replacement.append('&').append(character);
            }
            matcher.appendReplacement(sb, replacement.toString());
        }
        matcher.appendTail(sb);

        // Translate from '&' to '§' (section symbol)
        return ChatColor.translateAlternateColorCodes('&', sb.toString());
    }

    public static List<String> colorize(List<String> inputList) {
        if (inputList == null)
            return Collections.emptyList();
        return inputList.stream()
                .map(Messages::colorize)
                .toList();
    }

}
