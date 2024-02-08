package me.catalysmrl.catamines.command.abstraction;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.CommandManager;
import me.catalysmrl.catamines.api.mine.CataMine;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A command interface
 */
public interface CataCommand {

    /**
     * Gets the name of this commmand
     *
     * @return the command name
     */
    String getName();

    /**
     * Gets the aliases of this command
     *
     * @return the command aliases
     */
    default List<String> getAliases() {
        return Collections.emptyList();
    }

    /**
     * Gets the required permission to perform this command
     *
     * @return the command permission
     */
    Optional<String> getPermission();

    /**
     * Gets the predicate used to validate the number of arguments provided
     * to the command
     *
     * @return the argument checking predicate
     */
    Predicate<Integer> checkArgLength();

    /**
     * Returns true if the sender is authorized to use this command
     *
     * @param sender the sender using this command
     * @return true if the sender is authorized to use this command
     */
    boolean isAuthorized(CommandSender sender);

    /**
     * Returns true if this command can only be executed by players
     *
     * @return if only players can execute this command
     */
    boolean onlyPlayers();

    /**
     * {@link #execute(CataMines, CommandSender, List, CataMine)}
     */
    default void execute(CataMines plugin, CommandSender sender, List<String> args) throws CommandException {
        execute(plugin, sender, args, null);
    }

    /**
     * Method called on execution of this command if requirements are met
     * checked in the underlying command manager. Additionally injects
     * a CataMine object or null if required by the command.
     *
     * @param plugin the plugin instance
     * @param sender the command sender
     * @param args   the arguments provided
     * @param mine   the mine
     * @throws CommandException when unexpected arguments are passed or something goes wrong
     * @see CommandManager
     * @see AbstractCataMineCommand
     * @see CataMine
     */
    void execute(CataMines plugin, CommandSender sender, List<String> args, CataMine mine) throws CommandException;

    /**
     * Returns a list of command completions applicable for this command
     *
     * @param plugin The plugin instance
     * @param sender the command sender
     * @param args   the command arguments
     * @return a list of command completions
     */
    default List<String> tabComplete(CataMines plugin, CommandSender sender, List<String> args) {
        return Collections.emptyList();
    }

    /**
     * Gets the description of this command
     *
     * @return the commands description
     */
    String getDescription();

    /**
     * Gets the usage of this command
     *
     * @return the commands usage
     */
    String getUsage();
}
