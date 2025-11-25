package me.catalysmrl.catamines.utils.message;

import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Locale;

public class Message {
    // Universal
    public static final Message MINE_SAVE_EXCEPTION = Message.of("catamines.universal.mine.save-exception");
    public static final Message MINE_DELETE_EXCEPTION = Message.of("catamines.universal.mine.delete-exception");

    // CommandSystem
    public static final Message UNKNOWN_COMMAND = Message.of("catamines.commandsystem.unknown-command");
    public static final Message ONLY_PLAYERS = Message.of("catamines.commandsystem.only-players");
    public static final Message NO_PERMISSION = Message.of("catamines.commandsystem.no-permission");
    public static final Message MINE_NOT_EXISTS = Message.of("catamines.commandsystem.mine-not-exist");
    public static final Message QUERY_ALL = Message.of("catamines.commandsystem.query-all");

    // Generic commands
    public static final Message HELP_HEADER = Message.of("catamines.command.generic.help.header");
    public static final Message HELP_DESCRIPTION = Message.of("catamines.command.generic.help.description");
    public static final Message LIST_MINES_HEADER = Message.of("catamines.command.generic.list.header");
    public static final Message LIST_DESCRIPTION = Message.of("catamines.command.generic.list.description");

    // Mine commands
    public static final Message MINE_EXISTS = Message.of("catamines.command.mine.create.mine-exists");
    public static final Message INCOMPLETE_REGION = Message.of("catamines.command.mine.create.incomplete-region");
    public static final Message MINE_INVALID_NAME = Message.of("catamines.command.mine.create.invalid-name");
    public static final Message CREATE_SUCCESS = Message.of("catamines.command.mine.create.success");
    public static final Message CREATE_DESCRIPTION = Message.of("catamines.command.mine.create.description");

    public static final Message DELETE_EXCEPTION = Message.of("catamines.command.mine.delete.io-exception");
    public static final Message DELETE_SUCCESS = Message.of("catamines.command.mine.delete.success");
    public static final Message DELETE_DESCRIPTION = Message.of("catamines.command.mine.delete.description");

    public static final Message RESET_SUCCESS = Message.of("catamines.command.mine.reset.success");
    public static final Message RESET_DESCRIPTION = Message.of("catamines.command.mine.reset.description");

    public static final Message SET_INVALID_BLOCKSTATE = Message.of("catamines.command.mine.set.invalid-blockstate");
    public static final Message SET_INVALID_NUMBER = Message.of("catamines.command.mine.set.invalid-number");
    public static final Message SET_INVALID_CHANCE = Message.of("catamines.command.mine.set.invalid-chance");
    public static final Message SET_INVALID_REGION = Message.of("catamines.commmand.mine.set.invalid-region");
    public static final Message SET_INVALID_COMPOSITION = Message.of("catamines.command.mine.set.invalid-composition");
    public static final Message SET_SUCCESS = Message.of("catamines.command.mine.set.success");

    public static final Message RENAME_SUCCESS = Message.of("catamines.command.rename.success");
    public static final Message RENAME_DESCRIPTION = Message.of("catamines.command.rename.description");

    public static final Message DISPLAYNAME_SUCCESS = Message.of("catamines.command.displayname.success");
    public static final Message DISPLAYNAME_DESCRIPTION = Message.of("catamines.command.displayname.description");

    public static final Message TIMER_INVALID_FORMAT = Message.of("catamines.command.timer.invalid-format");
    public static final Message TIMER_SUCCESS = Message.of("catamines.command.timer.success");

    public static final Message RESETMODE_INVALID = Message.of("catamines.command.resetmode.invalid");

    // Mine region commands
    public static final Message REGION_EXISTS = Message.of("catamines.command.regions.create.region-exists");
    public static final Message REGION_CREATE_SUCCESS = Message.of("catamines.command.region.create.success");
    public static final Message REGION_CREATE_DESCRIPTION = Message.of("catamines.command.region.create.description");

    public static final Message REGION_NOT_EXISTS = Message.of("catamines.command.region.region-not-exists");
    public static final Message REGION_DELETE_SUCCESS = Message.of("catamines.command.region.delete.success");
    public static final Message REGION_DELETE_DESCRIPTION = Message.of("catamines.command.region.delete.description");

    // New commands
    public static final Message WARN_DESCRIPTION = Message.of("catamines.command.warn.description");
    public static final Message TELEPORT_DESCRIPTION = Message.of("catamines.command.teleport.description");
    public static final Message SYNC_DESCRIPTION = Message.of("catamines.command.sync.description");
    public static final Message START_DESCRIPTION = Message.of("catamines.command.start.description");
    public static final Message STOP_DESCRIPTION = Message.of("catamines.command.stop.description");

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
