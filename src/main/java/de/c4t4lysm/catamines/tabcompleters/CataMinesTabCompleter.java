package de.c4t4lysm.catamines.tabcompleters;

import com.sk89q.worldedit.world.block.BlockTypes;
import de.c4t4lysm.catamines.commands.commandhandler.CommandHandler;
import de.c4t4lysm.catamines.commands.commandhandler.FlagCommandsHandler;
import de.c4t4lysm.catamines.schedulers.MineManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CataMinesTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (!sender.hasPermission("catamines.*")) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            final List<String> completions = new ArrayList<>();
            return StringUtil.copyPartialMatches(args[0], new ArrayList<>(CommandHandler.getCommandNames()), completions);

        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("info")
                    || args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("unset")
                    || args[0].equalsIgnoreCase("start") || args[0].equalsIgnoreCase("stop")
                    || args[0].equalsIgnoreCase("setdelay") || args[0].equalsIgnoreCase("flag")
                    || args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("tp")
                    || args[0].equalsIgnoreCase("settp") || args[0].equalsIgnoreCase("setresettp")
                    || args[0].equalsIgnoreCase("gui")) {
                final List<String> completions = new ArrayList<>();
                return StringUtil.copyPartialMatches(args[1], MineManager.getInstance().getMineListNames(), completions);
            }

            if (args[0].equalsIgnoreCase("reload")) {
                final List<String> completions = new ArrayList<>();
                return StringUtil.copyPartialMatches(args[1], Arrays.asList("mines", "config", "messages", "properties"), completions);
            }
        } else if (args.length == 3) {

            if (!MineManager.getInstance().getMineListNames().contains(args[1])) {
                return Collections.emptyList();
            }

            if (args[0].equalsIgnoreCase("set")) {
                if (MineManager.getInstance().getMineListNames().contains(args[1])) {
                    final List<String> completions = new ArrayList<>();
                    List<String> blockList = new ArrayList<>();

                    for (Material material : Material.values()) {
                        if (material.isBlock()) {
                            blockList.add(material.toString().toLowerCase());
                        }
                    }
                    return StringUtil.copyPartialMatches(args[2], blockList, completions);
                }
            }

            if (args[0].equalsIgnoreCase("unset")) {
                if (MineManager.getInstance().getMineListNames().contains(args[1])) {
                    final List<String> completions = new ArrayList<>();
                    List<String> blockList = new ArrayList<>();
                    MineManager.getInstance().getMine(args[1]).getBlocks().forEach(block -> blockList.add(block.getMaterial().name()));
                    return StringUtil.copyPartialMatches(args[2], blockList, completions);
                }
            }

            if (args[0].equalsIgnoreCase("setDelay")) {
                if (MineManager.getInstance().getMineListNames().contains(args[1])) {
                    return StringUtil.copyPartialMatches(args[2], Collections.singletonList("seconds"), new ArrayList<>());
                }
            }

            if (args[0].equalsIgnoreCase("flag")) {
                return StringUtil.copyPartialMatches(args[2], FlagCommandsHandler.getFlagCommandNames(), new ArrayList<>());
            }

            if (args[0].equalsIgnoreCase("tp")) {
                return null;
            }

        } else if (args.length == 4) {

            if (!MineManager.getInstance().getMineListNames().contains(args[1])) {
                return Collections.emptyList();
            }

            if (args[0].equalsIgnoreCase("set")) {
                if (MineManager.getInstance().getMineListNames().contains(args[1])) {
                    if (BlockTypes.get(args[2]) != null) {
                        return StringUtil.copyPartialMatches(args[3], Collections.singletonList("e.g 50%"), new ArrayList<>());
                    }
                }
            }

            if (args[2].equalsIgnoreCase("replacemode") || args[2].equalsIgnoreCase("warn")
                    || args[2].equalsIgnoreCase("warnglobal") || args[2].equalsIgnoreCase("teleportplayers")
                    || args[2].equalsIgnoreCase("teleportplayerstoresetlocation")) {
                if (MineManager.getInstance().getMineListNames().contains(args[1])) {
                    final List<String> completions = new ArrayList<>();
                    return StringUtil.copyPartialMatches(args[3], Arrays.asList("true", "false"), completions);
                }
            }
        }

        return Collections.emptyList();
    }
}
