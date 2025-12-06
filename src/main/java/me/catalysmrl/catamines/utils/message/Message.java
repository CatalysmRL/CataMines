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
    public static final Message NO_MINES = Message.of("catamines.commandsystem.no-mines");
    public static final Message MINE_NOT_EXISTS = Message.of("catamines.commandsystem.mine-not-exist");
    public static final Message INVALID_TARGET = Message.of("catamines.commandsystem.invalid-target");
    public static final Message QUERY_ALL_HEADER = Message.of("catamines.commandsystem.query-all.header");
    public static final Message QUERY_ALL_ENTRY = Message.of("catamines.commandsystem.query-all.entry");
    public static final Message QUERY_ALL_FOOTER = Message.of("catamines.commandsystem.query-all.footer");

    // Generic commands
    public static final Message HELP_HEADER = Message.of("catamines.command.generic.help.header");
    public static final Message HELP_DESCRIPTION = Message.of("catamines.command.generic.help.description");
    public static final Message HELP_USAGE = Message.of("catamines.command.generic.help.usage");

    public static final Message LIST_MINES_HEADER = Message.of("catamines.command.generic.list.header");
    public static final Message LIST_DESCRIPTION = Message.of("catamines.command.generic.list.description");
    public static final Message LIST_USAGE = Message.of("catamines.command.generic.list.usage");

    public static final Message RELOAD_SUCCESS = Message.of("catamines.command.generic.reload.success");
    public static final Message RELOAD_DESCRIPTION = Message.of("catamines.command.generic.reload.description");
    public static final Message RELOAD_USAGE = Message.of("catamines.command.generic.reload.usage");

    public static final Message CONFIRM_HEADER = Message.of("catamines.command.generic.confirm.header");
    public static final Message CONFIRM_FOOTER = Message.of("catamines.command.generic.confirm.footer");
    public static final Message CONFIRM_DESCRIPTION = Message.of("catamines.command.generic.confirm.description");
    public static final Message CONFIRM_USAGE = Message.of("catamines.command.generic.confirm.usage");

    public static final Message UNDO_EMPTY = Message.of("catamines.command.generic.undo.empty");
    public static final Message UNDO_SUCCESS = Message.of("catamines.command.generic.undo.success");
    public static final Message UNDO_FOOTER = Message.of("catamines.command.generic.undo.footer");
    public static final Message UNDO_DESCRIPTION = Message.of("catamines.command.generic.undo.description");
    public static final Message UNDO_USAGE = Message.of("catamines.command.generic.undo.usage");

    public static final Message REDO_EMPTY = Message.of("catamines.command.generic.redo.empty");
    public static final Message REDO_SUCCESS = Message.of("catamines.command.generic.redo.success");
    public static final Message REDO_FOOTER = Message.of("catamines.command.generic.redo.footer");
    public static final Message REDO_DESCRIPTION = Message.of("catamines.command.generic.redo.description");
    public static final Message REDO_USAGE = Message.of("catamines.command.generic.redo.usage");

    // Mine commands
    public static final Message CREATE_MINE_EXISTS = Message.of("catamines.command.mine.create.mine-exists");
    public static final Message CREATE_INCOMPLETE_REGION = Message.of("catamines.command.mine.create.incomplete-region");
    public static final Message CREATE_INVALID_SCHEMATIC = Message.of("catamines.command.mine.create.invalid-schematic");
    public static final Message CREATE_INVALID_NAME = Message.of("catamines.command.mine.create.invalid-name");
    public static final Message CREATE_REGION_EXISTS = Message.of("catamines.command.mine.create.region-exists");
    public static final Message CREATE_REGION_SUCCESS = Message.of("catamines.command.mine.create.region-success");
    public static final Message CREATE_COMPOSITION_EXISTS = Message.of("catamines.command.mine.composition.create.composition-exists");
    public static final Message CREATE_COMPOSITION_SUCCESS = Message.of("catamines.command.mine.composition.create.success");
    public static final Message CREATE_SUCCESS = Message.of("catamines.command.mine.create.success");
    public static final Message CREATE_USAGE = Message.of("catamines.command.mine.create.usage");
    public static final Message CREATE_DESCRIPTION = Message.of("catamines.command.mine.create.description");

    public static final Message DEBUG_DESCRIPTION = Message.of("catamines.command.mine.debug.description");
    public static final Message DEBUG_USAGE = Message.of("catamines.command.mine.debug.usage");

    public static final Message DELETE_REGIONS_NOT_EXISTS = Message.of("catamines.command.mine.delete.region-not-exists");
    public static final Message DELETE_REGIONS_SUCCESS = Message.of("catamines.command.mine.delete.region-success");
    public static final Message DELETE_EXCEPTION = Message.of("catamines.command.mine.delete.io-exception");
    public static final Message DELETE_SUCCESS = Message.of("catamines.command.mine.delete.success");
    
    public static final Message DELETE_DESCRIPTION = Message.of("catamines.command.mine.delete.description");
    public static final Message DELETE_USAGE = Message.of("catamines.command.mine.delete.usage");

    public static final Message DISPLAYNAME_SUCCESS = Message.of("catamines.command.mine.displayname.success");
    public static final Message DISPLAYNAME_DESCRIPTION = Message.of("catamines.command.mine.displayname.description");
    public static final Message DISPLAYNAME_USAGE = Message.of("catamines.command.mine.displayname.usage");

    public static final Message GUI_DESCRIPTION = Message.of("catamines.command.mine.gui.description");
    public static final Message GUI_USAGE = Message.of("catamines.command.mine.gui.usage");

    public static final Message INFO_DESCRIPTION = Message.of("catamines.command.mine.info.description");
    public static final Message INFO_USAGE = Message.of("catamines.command.mine.info.usage");

    public static final Message REDEFINE_SUCCESS = Message.of("catamines.command.mine.redefine.success");
    public static final Message REDEFINE_DESCRIPTION = Message.of("catamines.command.mine.redefine.description");
    public static final Message REDEFINE_USAGE = Message.of("catamines.command.mine.redefine.usage");

    public static final Message RENAME_SUCCESS = Message.of("catamines.command.mine.rename.success");
    public static final Message RENAME_DESCRIPTION = Message.of("catamines.command.mine.rename.description");
    public static final Message RENAME_USAGE = Message.of("catamines.command.mine.rename.usage");

    public static final Message RESET_SUCCESS = Message.of("catamines.command.mine.reset.success");
    public static final Message RESET_DESCRIPTION = Message.of("catamines.command.mine.reset.description");
    public static final Message RESET_USAGE = Message.of("catamines.command.mine.reset.usage");

    public static final Message RESETMODE_INVALID = Message.of("catamines.command.mine.resetmode.invalid");
    public static final Message RESETMODE_DESCRIPTION = Message.of("catamines.command.mine.resetmode.description");
    public static final Message RESETMODE_USAGE = Message.of("catamines.command.mine.resetmode.usage");

    public static final Message RESETPERCENTAGE_SUCCESS = Message.of("catamines.command.mine.resetpercentage.success");
    public static final Message RESETPERCENTAGE_DESCRIPTION = Message
            .of("catamines.command.mine.resetpercentage.description");
    public static final Message RESETPERCENTAGE_USAGE = Message.of("catamines.command.mine.resetpercentage.usage");

    public static final Message SET_INVALID_BLOCKSTATE = Message.of("catamines.command.mine.set.invalid-blockstate");
    public static final Message SET_INVALID_NUMBER = Message.of("catamines.command.mine.set.invalid-number");
    public static final Message SET_INVALID_CHANCE = Message.of("catamines.command.mine.set.invalid-chance");
    public static final Message SET_INVALID_REGION = Message.of("catamines.command.mine.set.invalid-region");
    public static final Message SET_INVALID_COMPOSITION = Message.of("catamines.command.mine.set.invalid-composition");
    public static final Message SET_SUCCESS = Message.of("catamines.command.mine.set.success");
    public static final Message SET_DESCRIPTION = Message.of("catamines.command.mine.set.description");
    public static final Message SET_USAGE = Message.of("catamines.command.mine.set.usage");

    public static final Message SETRESETTELEPORT_SUCCESS = Message
            .of("catamines.command.mine.setresetteleport.success");
    public static final Message SETRESETTELEPORT_DESCRIPTION = Message
            .of("catamines.command.mine.setresetteleport.description");
    public static final Message SETRESETTELEPORT_USAGE = Message.of("catamines.command.mine.setresetteleport.usage");

    public static final Message SETTELEPORT_SUCCESS = Message.of("catamines.command.mine.setteleport.success");
    public static final Message SETTELEPORT_DESCRIPTION = Message.of("catamines.command.mine.setteleport.description");
    public static final Message SETTELEPORT_USAGE = Message.of("catamines.command.mine.setteleport.usage");

    public static final Message START_SUCCESS = Message.of("catamines.command.mine.start.success");
    public static final Message START_DESCRIPTION = Message.of("catamines.command.mine.start.description");
    public static final Message START_USAGE = Message.of("catamines.command.mine.start.usage");

    public static final Message STARTTASKS_SUCCESS = Message.of("catamines.command.mine.starttasks.success");
    public static final Message STARTTASKS_DESCRIPTION = Message.of("catamines.command.mine.starttasks.description");
    public static final Message STARTTASKS_USAGE = Message.of("catamines.command.mine.starttasks.usage");

    public static final Message STOP_SUCCESS = Message.of("catamines.command.mine.stop.success");
    public static final Message STOP_DESCRIPTION = Message.of("catamines.command.mine.stop.description");
    public static final Message STOP_USAGE = Message.of("catamines.command.mine.stop.usage");

    public static final Message STOPTASKS_SUCCESS = Message.of("catamines.command.mine.stoptasks.success");
    public static final Message STOPTASKS_DESCRIPTION = Message.of("catamines.command.mine.stoptasks.description");
    public static final Message STOPTASKS_USAGE = Message.of("catamines.command.mine.stoptasks.usage");

    public static final Message SYNC_SUCCESS = Message.of("catamines.command.mine.sync.success");
    public static final Message SYNC_DESCRIPTION = Message.of("catamines.command.mine.sync.description");
    public static final Message SYNC_USAGE = Message.of("catamines.command.mine.sync.usage");

    public static final Message TELEPORT_SUCCESS = Message.of("catamines.command.mine.teleport.success");
    public static final Message TELEPORT_DESCRIPTION = Message.of("catamines.command.mine.teleport.description");
    public static final Message TELEPORT_USAGE = Message.of("catamines.command.mine.teleport.usage");

    public static final Message TELEPORTPLAYERS_SUCCESS = Message.of("catamines.command.mine.teleportplayers.success");
    public static final Message TELEPORTPLAYERS_DESCRIPTION = Message
            .of("catamines.command.mine.teleportplayers.description");
    public static final Message TELEPORTPLAYERS_USAGE = Message.of("catamines.command.mine.teleportplayers.usage");

    public static final Message TIMER_INVALID_FORMAT = Message.of("catamines.command.mine.timer.invalid-format");
    public static final Message TIMER_SUCCESS = Message.of("catamines.command.mine.timer.success");
    public static final Message TIMER_DESCRIPTION = Message.of("catamines.command.mine.timer.description");
    public static final Message TIMER_USAGE = Message.of("catamines.command.mine.timer.usage");

    public static final Message UNSET_SUCCESS = Message.of("catamines.command.mine.unset.success");
    public static final Message UNSET_DESCRIPTION = Message.of("catamines.command.mine.unset.description");
    public static final Message UNSET_USAGE = Message.of("catamines.command.mine.unset.usage");

    public static final Message WARN_SUCCESS = Message.of("catamines.command.mine.warn.success");
    public static final Message WARN_DESCRIPTION = Message.of("catamines.command.mine.warn.description");
    public static final Message WARN_USAGE = Message.of("catamines.command.mine.warn.usage");

    public static final Message REGIONS_RESET_SUCCESS = Message.of("catamines.command.mine.regions.reset.success");
    public static final Message REGIONS_RESET_DESCRIPTION = Message
            .of("catamines.command.mine.regions.reset.description");
    public static final Message REGIONS_RESET_USAGE = Message.of("catamines.command.mine.regions.reset.usage");

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

    public void sendList(CommandSender sender, Object... args) {
        MessageService.sendList(sender, key, args);
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
