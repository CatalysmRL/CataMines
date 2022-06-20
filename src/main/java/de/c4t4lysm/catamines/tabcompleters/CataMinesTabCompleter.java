package de.c4t4lysm.catamines.tabcompleters;

import de.c4t4lysm.catamines.commands.commandhandler.CommandHandler;
import de.c4t4lysm.catamines.commands.commandhandler.FlagCommandsHandler;
import de.c4t4lysm.catamines.schedulers.MineManager;
import de.c4t4lysm.catamines.utils.mine.components.CataMineResetMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CataMinesTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (!sender.hasPermission("catamines.*")) {
            return Collections.emptyList();
        }

        switch (args.length) {
            case 1: {
                return StringUtil.copyPartialMatches(args[0], new ArrayList<>(CommandHandler.getCommandNames()), new ArrayList<>());
            }
            case 2:
                List<String> correctArgs = Arrays.asList("delete", "info", "set", "unset", "start", "stop", "setdelay", "flag",
                        "reset", "tp", "settp", "setresettp", "gui", "resetmode", "resetpercentage", "redefine");
                if (correctArgs.contains(args[0].toLowerCase())) {
                    return StringUtil.copyPartialMatches(args[1], MineManager.getInstance().getMineListNames(), new ArrayList<>());
                } else if (args[0].equalsIgnoreCase("reload")) {
                    return StringUtil.copyPartialMatches(args[1], Arrays.asList("mines", "config", "messages", "properties"), new ArrayList<>());
                }
                break;
            case 3: {
                if (!MineManager.getInstance().getMineListNames().contains(args[1])) {
                    return Collections.emptyList();
                }

                List<String> completions = new ArrayList<>();
                switch (args[0].toLowerCase()) {
                    case "set":
                        ArrayList<String> blocks = new ArrayList<>();
                        for (Material material : Material.values()) {
                            if (material.isBlock() && material.isSolid()) {
                                blocks.add(material.toString().toLowerCase());
                            }
                        }


                        return StringUtil.copyPartialMatches(args[2], blocks, completions);
                    case "unset":
                        List<String> blockList = new ArrayList<>();
                        MineManager.getInstance().getMine(args[1]).getBlocks().forEach(block -> blockList.add(block.getBlockData().getMaterial().name()));
                        return StringUtil.copyPartialMatches(args[2], blockList, completions);
                    case "setdelay":
                        return StringUtil.copyPartialMatches(args[2], Collections.singletonList("seconds"), completions);
                    case "flag":
                        return StringUtil.copyPartialMatches(args[2], FlagCommandsHandler.getFlagCommandNames(), completions);
                    case "tp":
                        return null;
                    case "resetmode":
                        return StringUtil.copyPartialMatches(args[2], Arrays.stream(CataMineResetMode.values()).map(Enum::name).collect(Collectors.toList()), completions);
                }
                break;
            }
            case 4:

                if (!MineManager.getInstance().getMineListNames().contains(args[1])) {
                    return Collections.emptyList();
                }

                switch (args[0].toLowerCase()) {
                    case "replacemode":
                    case "warn":
                    case "warnglobal":
                    case "teleportplayers":
                    case "teleportplayerstoresetlocation":
                        return StringUtil.copyPartialMatches(args[3], Arrays.asList("true", "false"), new ArrayList<>());
                }
                break;
        }

        return Collections.emptyList();
    }
}
