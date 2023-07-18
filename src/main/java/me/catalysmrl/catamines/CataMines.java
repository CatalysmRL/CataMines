package me.catalysmrl.catamines;

import me.catalysmrl.catamines.command.CommandManager;
import me.catalysmrl.catamines.listeners.BlockListeners;
import me.catalysmrl.catamines.managers.MineManager;
import me.catalysmrl.catamines.mine.components.composition.CataMineBlock;
import me.catalysmrl.catamines.mine.mines.RegionCataMine;
import me.catalysmrl.catamines.mine.placeholders.CataMinePlaceHolders;
import me.catalysmrl.catamines.utils.helper.CompatibilityProvider;
import me.catalysmrl.catamines.utils.message.LangSystem;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

public final class CataMines extends JavaPlugin {

    private static CataMines instance;
    public static CataMines getInstance() {
        return instance;
    }

    static {
        ConfigurationSerialization.registerClass(RegionCataMine.class);
        ConfigurationSerialization.registerClass(CataMineBlock.class);
    }

    private Path dataPath;
    private LangSystem langSystem;
    private MineManager mineManager;
    private CommandManager commandManager;

    @Override
    public void onLoad() {
        instance = this;

        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        CompatibilityProvider.checkCompatibility();

        dataPath = getDataFolder().toPath();
        langSystem = new LangSystem(this);

        mineManager = new MineManager(this);
        registerCommands();
        registerListeners();

        if (CompatibilityProvider.isPapiEnabled()) {
            new CataMinePlaceHolders(this).register();
        }

        setupMetrics();
    }

    @Override
    public void onDisable() {
        mineManager.shutDown();
        instance = null;
    }

    private void setupMetrics() {
        final Metrics metrics = new Metrics(this, 12889);
        metrics.addCustomChart(new SimplePie("we_implementation", () ->
                CompatibilityProvider.isFaweEnabled() ? "FastAsyncWorldEdit" : "WorldEdit"));

        metrics.addCustomChart(new SingleLineChart("mines", () -> mineManager.getMines().size()));
    }

    private void registerCommands() {
        commandManager = new CommandManager(this);

        PluginCommand command = getCommand("catamines");
        if (command != null) {
            command.setExecutor(commandManager);
        }
    }

    private void registerListeners() {
        getLogger().info("Registering listeners");
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new BlockListeners(mineManager), this);
    }

    public LangSystem getLangSystem() {
        return langSystem;
    }

    public MineManager getMineManager() {
        return mineManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public Path getDataPath() {
        return dataPath;
    }
}
