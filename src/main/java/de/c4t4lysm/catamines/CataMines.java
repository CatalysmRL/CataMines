package de.c4t4lysm.catamines;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import de.c4t4lysm.catamines.commands.CataMinesHelpCommand;
import de.c4t4lysm.catamines.commands.cmcommands.*;
import de.c4t4lysm.catamines.commands.commandhandler.CommandHandler;
import de.c4t4lysm.catamines.commands.commandhandler.FlagCommandsHandler;
import de.c4t4lysm.catamines.listeners.BlockListeners;
import de.c4t4lysm.catamines.listeners.PlayerJoinListener;
import de.c4t4lysm.catamines.schedulers.MineManager;
import de.c4t4lysm.catamines.tabcompleters.CataMinesTabCompleter;
import de.c4t4lysm.catamines.utils.UpdateChecker;
import de.c4t4lysm.catamines.utils.configuration.FileManager;
import de.c4t4lysm.catamines.utils.menusystem.PlayerMenuUtility;
import de.c4t4lysm.catamines.utils.menusystem.menulisteners.MenuListener;
import de.c4t4lysm.catamines.utils.mine.components.CataMineBlock;
import de.c4t4lysm.catamines.utils.mine.mines.CuboidCataMine;
import de.c4t4lysm.catamines.utils.mine.placeholders.CataMinePlaceHolders;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public final class CataMines extends JavaPlugin {

    public static String PREFIX = "§6[§bCata&aMines§6] §7";
    private static CataMines plugin;
    private static HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<Player, PlayerMenuUtility>();
    public boolean updateAvailable = false;
    public String availableVersion = "";
    public boolean placeholderAPI = false;
    private FileManager fileManager;
    private WorldEditPlugin worldEditPlugin;

    public static CataMines getInstance() {
        return plugin;
    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player player) {
        if (playerMenuUtilityMap.containsKey(player)) {
            return playerMenuUtilityMap.get(player);
        }
        PlayerMenuUtility playerMenuUtility = new PlayerMenuUtility(player);
        playerMenuUtilityMap.put(player, playerMenuUtility);

        return playerMenuUtility;
    }

    public static void removePlayerMenuUtility(Player player) {
        playerMenuUtilityMap.remove(player);
    }

    public static HashMap<Player, PlayerMenuUtility> getPlayerMenuUtilityMap() {
        return playerMenuUtilityMap;
    }

    @Override
    public void onEnable() {
        plugin = this;
        worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        ConfigurationSerialization.registerClass(CuboidCataMine.class);
        ConfigurationSerialization.registerClass(CataMineBlock.class);

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        this.fileManager = new FileManager(this);

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        Bukkit.getScheduler().runTaskLater(this, () -> {
            new MineManager();
            registerCommands();
            registerListeners();
        }, 5 * 20L);

        new UpdateChecker(getInstance(), 96457).getVersion(version -> {
            if (!getInstance().getDescription().getVersion().equalsIgnoreCase(version)) {
                updateAvailable = true;
                availableVersion = version;
                getLogger().info("There is a new version of CataMines available: " + version);
            }
        });
        int pluginId = 12889;
        new Metrics(this, pluginId);

        if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new CataMinePlaceHolders(this).register();
            placeholderAPI = true;
        }

    }

    @Override
    public void onDisable() {
        plugin = null;
        playerMenuUtilityMap = null;
        fileManager = null;
        worldEditPlugin = null;
    }

    private void registerCommands() {
        getLogger().info("Starting mines and registering commands. Running version " + getDescription().getVersion());

        CommandHandler commandHandler = new CommandHandler();
        commandHandler.register("help", new CataMinesHelpCommand());

        commandHandler.register("create", new CreateCommand());
        commandHandler.register("delete", new DeleteCommand());
        commandHandler.register("redefine", new RedefineCommand());
        commandHandler.register("info", new InfoCommand());
        commandHandler.register("list", new ListCommand());
        commandHandler.register("resetmode", new ResetMode());
        commandHandler.register("setdelay", new SetDelayCommand());
        commandHandler.register("resetpercentage", new ResetPercentage());
        commandHandler.register("set", new SetCommand());
        commandHandler.register("unset", new UnsetCommand());
        commandHandler.register("reset", new ResetCommand());
        commandHandler.register("flag", new FlagCommandsHandler());
        commandHandler.register("start", new StartCommand());
        commandHandler.register("stop", new StopCommand());
        commandHandler.register("starttasks", new StartTasksCommand());
        commandHandler.register("stoptasks", new StopTasksCommand());
        commandHandler.register("tp", new TeleportCommand());
        commandHandler.register("settp", new SetTeleportCommand());
        commandHandler.register("setresettp", new SetResetTeleportCommand());
        commandHandler.register("reload", new ReloadCommand());

        commandHandler.register("gui", new GuiCommand());
        getCommand("catamines").setExecutor(commandHandler);

        getCommand("catamines").setTabCompleter(new CataMinesTabCompleter());
    }

    private void registerListeners() {
        getLogger().info("Registering listeners");
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new BlockListeners(), this);
        pm.registerEvents(new MenuListener(), this);
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public String getDefaultString(String str) {
        return fileManager.getDefaultString(str);
    }

    public String getLangString(String str) {
        return fileManager.getLangString(str);
    }

    public List<String> getLangStringList(String str) {
        return fileManager.getLangStringList(str);
    }

    public void reloadLanguages() {
        reloadConfig();
        fileManager.setupLanguageFiles();
    }

    public WorldEditPlugin getWorldEditPlugin() {
        return worldEditPlugin;
    }
}
