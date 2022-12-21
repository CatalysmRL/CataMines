package me.catalysmrl.legacycatamines.mine.components;

import me.catalysmrl.legacycatamines.utils.Utils;
import me.catalysmrl.legacycatamines.mine.AbstractCataMine;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

import java.util.*;

public class CataMineExecutor {

    private final AbstractCataMine mine;
    private List<String> defaultExecution = new ArrayList<>();
    private Map<Integer, List<String>> executionMap = new HashMap<>();

    public CataMineExecutor(AbstractCataMine mine) {
        this.mine = mine;
    }

    public void tickExecution() {
        int count = mine.getCountdown();
        if (!executionMap.containsKey(count)) {
            return;
        }

        for (String execute : executionMap.get(count)) {
            execute(execute);
        }
    }

    public void messageExecute(String action, String message) {

        final String finalMessage = Utils.setPlaceholders(message, mine);

        switch (action) {
            case "m":
                mine.getPlayersInDistance().forEach(player -> Arrays.stream(finalMessage.split("/n")).forEach(player::sendMessage));
                break;
            case "mglobal":
                Bukkit.getOnlinePlayers().forEach(player -> Arrays.stream(finalMessage.split("/n")).forEach(player::sendMessage));
                break;
            case "actionbar":
                mine.getPlayersInDistance().forEach(player -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(finalMessage)));
        }
    }

    public void commandExecute(String sender, String executionString) {

        final String command = Utils.setPlaceholders(executionString, mine);

        switch (sender) {
            case "p":
                mine.getPlayersInDistance().forEach(player -> player.performCommand(command));
                break;
            case "c":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                break;
            case "pAll":
                Bukkit.getOnlinePlayers().forEach(player -> player.performCommand(command.replaceAll("%player%", player.getName())));
                break;
            case "cAll":
                Bukkit.getOnlinePlayers().forEach(player -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", player.getName())));
                break;
        }
    }

    public void mineExecute(String execution) {
        switch (execution) {
            case "teleportPlayers":
                mine.teleportPlayers();
                break;
            case "reset":
                mine.reset();
                break;
        }
    }

    public void execute(String execution) {
        String executionString = stripExecution(execution);
        if (executionString == null) return;

        if (execution.startsWith("mine:")) {
            mineExecute(executionString);
        } else if (execution.startsWith("m:") || execution.startsWith("mglobal:") || execution.startsWith("actionbar:")) {
            messageExecute(execution.substring(0, execution.indexOf(":")), executionString);
        } else if (execution.startsWith("c:") || execution.startsWith("p:") || execution.startsWith("cAll:") || execution.startsWith("pAll:")) {
            commandExecute(execution.substring(0, execution.indexOf(":")), executionString);
        }
    }

    public String stripExecution(String execution) {
        int i = execution.indexOf(":");
        if (i == -1 || execution.length() < i + 2) {
            return null;
        }

        String result = execution.substring(i + 1);
        if (result.charAt(0) == ' ') result = result.substring(1);
        return result;
    }

    public List<String> getDefaultExecution() {
        return defaultExecution;
    }

    public void setDefaultExecution(List<String> defaultExecution) {
        this.defaultExecution = defaultExecution;
    }

    public Map<Integer, List<String>> getExecutionMap() {
        return executionMap;
    }

    public void setExecutionMap(Map<Integer, List<String>> executionMap) {
        this.executionMap = executionMap;
    }
}
