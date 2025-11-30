package me.catalysmrl.catamines.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableMap;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.abstraction.Command;
import me.catalysmrl.catamines.command.abstraction.CommandContext;
import me.catalysmrl.catamines.command.abstraction.CommandException;
import me.catalysmrl.catamines.commands.generic.*;
import me.catalysmrl.catamines.commands.mine.*;
import me.catalysmrl.catamines.commands.mine.regions.*;
import me.catalysmrl.catamines.utils.message.Message;

public class CommandManager implements TabExecutor {

    private final CataMines plugin;
    private final Map<String, Command> commandMap;

    public CommandManager(CataMines plugin) {
        this.plugin = plugin;

        commandMap = ImmutableMap.<String, Command>builder()
                .put("help", new HelpCommand())
                .put("list", new ListCommand())
                .put("reload", new ReloadCommand())
                .put("create", new CreateCommand())
                .put("debug", new DebugCommand())
                .put("delete", new DeleteCommand())
                .put("displayname", new DisplayNameCommand())
                .put("info", new InfoCommand())
                .put("redefine", new RedefineCommand())
                .put("regions", new RegionsCommand())
                .put("rename", new RenameCommand())
                .put("reset", new ResetCommand())
                .put("resetmode", new ResetModeCommand())
                .put("set", new SetCommand())
                .put("timer", new TimerCommand())
                .put("unset", new UnsetCommand())
                .put("gui", new GuiCommand())
                .put("resetpercentage", new ResetPercentageCommand())
                .put("start", new StartCommand())
                .put("starttasks", new StartTasksCommand())
                .put("stop", new StopCommand())
                .put("stoptasks", new StopTasksCommand())
                .put("sync", new SyncCommand())
                .put("teleport", new TeleportCommand())
                .put("warn", new WarnCommand())
                .put("setteleport", new SetTeleportCommand())
                .put("setresetteleport", new SetResetTeleportCommand())
                .build();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command,
            @NotNull String label, @NotNull String[] args) {

        String subCmd;

        if (args.length == 0) {
            subCmd = "help";
        } else {
            subCmd = args[0].toLowerCase(Locale.ROOT);
        }

        Command cataCommand = getCommand(subCmd);
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

        List<String> rawArgs = Arrays.asList(args);
        List<String> subArgs = rawArgs.size() > 1 ? rawArgs.subList(1, rawArgs.size()) : List.of();
        CommandContext ctx = new CommandContext(subArgs);

        try {
            cataCommand.execute(plugin, sender, ctx);
        } catch (CommandException e) {
            e.handle(sender, cataCommand);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command,
            @NotNull String label, @NotNull String[] args) {

        final List<Command> commands = commandMap.values().stream()
                .filter(c -> c.isAuthorized(commandSender))
                .toList();

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0],
                    commands.stream().map(Command::getName).collect(Collectors.toList()), new ArrayList<>());
        } else if (args.length > 1) {
            return commands.stream()
                    .filter(c -> c.getName().equalsIgnoreCase(args[0])
                            || c.getAliases().contains(args[0].toLowerCase(Locale.ROOT)))
                    .findFirst()
                    .map(c -> c.tabComplete(plugin, commandSender,
                            new CommandContext(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)))))
                    .orElse(Collections.emptyList());
        }

        return Collections.emptyList();
    }

    public Command getCommand(String commandName) {
        if (commandMap.containsKey(commandName))
            return commandMap.get(commandName);

        return commandMap.values().stream()
                .filter(command -> command.getName().equals(commandName))
                .findFirst().orElse(null);
    }

    public Map<String, Command> getCommandMap() {
        return commandMap;
    }
}
