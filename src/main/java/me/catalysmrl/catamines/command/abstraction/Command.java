package me.catalysmrl.catamines.command.abstraction;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.utils.CommandContext;
import me.catalysmrl.catamines.command.utils.CommandException;
import me.catalysmrl.catamines.utils.message.Message;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A command interface
 */
public interface Command {

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
     * Executing the command
     *
     * @param plugin  the plugin instance
     * @param sender  the command sender
     * @param context the command context
     * @throws CommandException if there is an exception when executing the command
     */
    void execute(CataMines plugin, CommandSender sender, CommandContext context) throws CommandException;

    /**
     * Returns a list of command completions applicable for this command
     *
     * @param plugin  the plugin instance
     * @param sender  the command sender
     * @param context the command arguments
     * @return a list of command completions
     */
    default List<String> tabComplete(CataMines plugin, CommandSender sender, CommandContext context) {
        return Collections.emptyList();
    }

    /**
     * Gets the description of this command
     *
     * @return the commands description
     */
    Message getDescription();

    /**
     * Gets the usage of this command
     *
     * @return the commands usage
     */
    Message getUsage();
}
