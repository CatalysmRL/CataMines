package me.catalysmrl.catamines;

import me.catalysmrl.catamines.command.CommandManager;
import me.catalysmrl.catamines.listeners.BlockListeners;
import me.catalysmrl.catamines.managers.MineManager;
import me.catalysmrl.catamines.mine.placeholders.CataMinePlaceHolders;
import me.catalysmrl.catamines.shaded.metrics.Metrics;
import me.catalysmrl.catamines.utils.helper.CompatibilityProvider;
import me.catalysmrl.catamines.utils.message.LocaleBootstrap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CataMines extends JavaPlugin {

    private static CataMines INSTANCE;

    public static CataMines getInstance() {
        return INSTANCE;
    }

    private MineManager mineManager;
    private CommandManager commandManager;

    @Override
    public void onLoad() {
        INSTANCE = this;

        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        CompatibilityProvider.checkCompatibility();

        new LocaleBootstrap(this).init();

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
        INSTANCE = null;
        commandManager = null;

        // Properly disable MineManager
        mineManager.shutDown();
        mineManager = null;
    }

    private void setupMetrics() {
        final Metrics metrics = new Metrics(this, 12889);
        metrics.addCustomChart(new Metrics.SimplePie("we_implementation", () ->
                CompatibilityProvider.isFaweEnabled() ? "FastAsyncWorldEdit" : "WorldEdit"));

        metrics.addCustomChart(new Metrics.SingleLineChart("mines", () -> mineManager.getMines().size()));
    }

    private void registerCommands() {
        commandManager = new CommandManager(this);

        PluginCommand command = getCommand("catamines");
        if (command == null) {
            getLogger().severe("***************************************");
            getLogger().severe("Could not register commands. All plugin");
            getLogger().severe("functions may still work apart from commands");
            getLogger().severe("***************************************");
            return;
        }

        command.setExecutor(commandManager);
    }

    private void registerListeners() {
        getLogger().info("Registering listeners");
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new BlockListeners(mineManager), this);
    }

    public MineManager getMineManager() {
        return mineManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }
}
