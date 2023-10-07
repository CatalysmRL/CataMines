package me.catalysmrl.catamines.command;

import com.google.common.collect.ImmutableMap;
import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.CataCommand;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.commands.generic.HelpCommand;
import me.catalysmrl.catamines.commands.generic.ListCommand;
import me.catalysmrl.catamines.commands.generic.ReloadCommand;
import me.catalysmrl.catamines.commands.mine.*;
import me.catalysmrl.catamines.utils.message.Message;
import me.catalysmrl.catamines.utils.message.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class CommandManager implements TabExecutor {

    private final CataMines plugin;
    private final Map<String, CataCommand> commandMap;

    public CommandManager(CataMines plugin) {
        this.plugin = plugin;

        commandMap = ImmutableMap.<String, CataCommand>builder()
                .put("help", new HelpCommand())
                .put("list", new ListCommand())
                .put("reload", new ReloadCommand())
                .put("info", new InfoCommand())
                .put("debug", new DebugCommand())
                .put("create", new CreateCommand())
                .put("delete", new DeleteCommand())
                .put("reset", new ResetCommand())
                .put("set", new SetCommand())
                .put("rename", new RenameCommand())
                .put("displayname", new DisplayNameCommand())
                .build();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        String subCmd;

        if (args.length == 0) {
            subCmd = "help";
        } else {
            subCmd = args[0].toLowerCase(Locale.ROOT);
        }

        CataCommand cataCommand = getCommand(subCmd);
        if (cataCommand == null) {
            Message.UNKNOWN_COMMAND.send(sender);
            return true;
        }

        if (cataCommand.onlyPlayers() && !(sender instanceof Player)) {
            Message.ONLY_PLAYERS.send(sender);
            return true;
        }

        if (!cataCommand.isAuthorized(sender)) {
            Message.NO_PERMISSION.send(sender);
            return true;
        }

        List<String> strippedArgs = new ArrayList<>(Arrays.asList(args));
        if (!strippedArgs.isEmpty()) {
            strippedArgs.remove(0);
        }

        if (!cataCommand.checkArgLength().test(strippedArgs.size())) {
            Messages.send(sender, cataCommand.getUsage());
            return true;
        }

        try {
            cataCommand.execute(plugin, sender, strippedArgs);
        } catch (CommandException e) {
            e.handle(sender, cataCommand);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        final List<CataCommand> commands = commandMap.values().stream()
                .filter(c -> c.isAuthorized(commandSender))
                .toList();

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], commands.stream().map(CataCommand::getName).collect(Collectors.toList()), new ArrayList<>());
        } else if (args.length > 1) {
            return commands.stream()
                    .filter(c -> c.getName().equalsIgnoreCase(args[0]) || c.getAliases().contains(args[0].toLowerCase(Locale.ROOT)))
                    .findFirst()
                    .map(c -> c.tabComplete(plugin, commandSender, Arrays.asList(Arrays.copyOfRange(args, 1, args.length))))
                    .orElse(Collections.emptyList());
        }

        return Collections.emptyList();
    }

    public CataCommand getCommand(String commandName) {
        if (commandMap.containsKey(commandName)) return commandMap.get(commandName);

        return commandMap.values().stream()
                .filter(command -> command.getName().equals(commandName))
                .findFirst().orElse(null);
    }

    public Map<String, CataCommand> getCommandMap() {
        return commandMap;
    }
}
