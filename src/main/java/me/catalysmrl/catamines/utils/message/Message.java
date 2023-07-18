package me.catalysmrl.catamines.utils.message;

import me.catalysmrl.catamines.CataMines;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;

public enum Message {

    /*
     * CommandSystem
     */
    NO_PERMISSION("catamines.commandsystem.no-permission"),
    UNKNOWN_COMMAND("catamines.commandsystem.unknown-command"),
    ONLY_PLAYERS("catamines.commandsystem.only-players"),
    MINE_NOT_EXIST("catamines.commandsystem.mine-not-exist"),
    QUERY_ALL("catamines.commandsystem.query-all"),

    /*
     * Generic commands
     */
    // Help command
    HELP_HEADER("catamines.command.generic.help.header"),
    HELP_DESCRIPTION("catamines.command.generic.help.description"),
    // List command
    LIST_MINES_HEADER("catamines.command.generic.list.header"),
    LIST_DESCRIPTION("catamines.command.generic.list.description"),

    /*
     * Mine commands
     */
    // Create command
    MINE_EXIST("catamines.command.mine.create.mine-exists"),
    INCOMPLETE_REGION("catamines.command.mine.create.incomplete-region"),
    MINE_INVALID_NAME("catamines.command.mine.create.invalid-name"),
    CREATE("catamines.command.mine.create"),
    CREATE_DESCRIPTION("catamines.command.mine.create.description"),

    // Delete command
    DELETE_EXCEPTION("catamines.command.mine.delete.io-exception"),
    DELETE("catamines.command.mine.delete"),
    DELETE_DESCRIPTION("catamines.command.mine.delete.description"),

    // Reset command
    RESET("catamines.command.mine.reset"),
    RESET_DESCRIPTION("catamines.command.mine.reset.description"),

    // Set command
    SET_INVALID_BLOCKDATA("catamines.command.mine.set.invalid-blockdata"),
    SET_INVALID_NUMBER("catamines.command.mine.set.invalid-number"),
    SET_INVALID_CHANCE("catamines.command.mine.set.invalid-chance"),
    SET("catamines.command.mine.set");


    private final String key;

    Message(String key) {
        this.key = key;
    }

    /**
     * Retrieves the message from the ResourceBundle in LangSystem
     * using the key stored in this enum.
     *
     * @param sender the sender to send the message to
     */
    public void send(CommandSender sender) {
        Messages.sendColorized(sender, getMessage());
    }

    /**
     * Retrieves the message from the ResourceBundle in LangSystem
     * using the key stored in this enum. The objects passed into
     * this method are formatted and replaces the placeholders
     * in the message
     *
     * @param sender the sender to send the message to
     * @param args   arguments to format
     */
    public void send(CommandSender sender, Object... args) {
        Messages.sendColorized(sender, getMessage(args));
    }

    public String getMessage() {
        return CataMines.getInstance().getLangSystem().getTranslatedMessage(key);
    }

    public String getMessage(Object... args) {
        return format(getMessage(), args);
    }

    private String format(String message, Object... args) {
        return MessageFormat.format(getMessage(), args);
    }
}
