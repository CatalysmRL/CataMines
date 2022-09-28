package de.c4t4lysm.catamines.utils.configuration;

import de.c4t4lysm.catamines.CataMines;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileManager {

    private final CataMines plugin;
    private final File dataFolder;
    private FileConfig langCfg;
    private CustomConfigFile propertiesCfg;

    public FileManager(CataMines plugin) {
        this.plugin = plugin;
        this.dataFolder = plugin.getDataFolder();
        setupFiles();
    }

    public String getDefaultString(String str) {
        if (!propertiesCfg.contains(str)) {
            return "Could not find " + str;
        }
        return propertiesCfg.getString(str);
    }

    public String getLangString(String str) {
        if (!langCfg.contains(str)) {
            return "Error loading: " + str + ", " + plugin.getConfig().getString("language");
        }
        return ChatColor.translateAlternateColorCodes('&', langCfg.getString(str));
    }

    public List<Integer> getDefaultIntegers(String str) {
        if (!propertiesCfg.contains(str)) {
            return Arrays.asList(600, 300, 60, 20, 5);
        }
        return propertiesCfg.getIntegerList(str);
    }

    public List<String> getLangStringList(String str) {
        if (!langCfg.contains(str)) {
            return Arrays.asList("Could not load ", str, "language: " + plugin.getConfig().getString("language"));
        }
        List<String> translatedList = new ArrayList<>();
        langCfg.getStringList(str).forEach(s -> translatedList.add(ChatColor.translateAlternateColorCodes('&', s)));
        return translatedList;
    }

    public void setupFiles() {
        setupConfig();
        setupMinesFolder();
        setupCustomFiles();
        setupLanguageFiles();
    }

    public void setupConfig() {

        plugin.getLogger().info("Loading config file");
        new CustomConfigFile(plugin, new File(dataFolder, "config.yml"), "config.yml");

        plugin.reloadConfig();

        CataMines.PREFIX = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix"));
    }

    public void setupMinesFolder() {
        File minesDirectory = new File(plugin.getDataFolder() + "/mines");
        if (!minesDirectory.exists() && !minesDirectory.mkdirs()) {
            plugin.getLogger().severe("Could not create mines folder, plugin disabled!");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    public void setupCustomFiles() {

        this.propertiesCfg = new CustomConfigFile(plugin, new File(dataFolder, "default_properties.yml"), "default_properties.yml");
        this.propertiesCfg.saveConfig();

    }

    public void setupLanguageFiles() {

        new CustomConfigFile(plugin, new File(dataFolder + "/languages", "messages_en.yml"), "messages_en.yml", StandardCopyOption.REPLACE_EXISTING);
        new CustomConfigFile(plugin, new File(dataFolder + "/languages", "messages_es.yml"), "messages_es.yml", StandardCopyOption.REPLACE_EXISTING);
        new CustomConfigFile(plugin, new File(dataFolder + "/languages", "messages_cn.yml"), "messages_cn.yml", StandardCopyOption.REPLACE_EXISTING);

        new CustomConfigFile(plugin, new File(dataFolder + "/languages", "messages_custom.yml"), "messages_custom.yml");

        File langFile = new File(dataFolder + "/languages", "messages_" + plugin.getConfig().getString("language").toLowerCase() + ".yml");
        if (!langFile.exists()) {
            plugin.getLogger().severe("The language you chose does not exist: " + langCfg.getName() + ", changing to EN");
            langCfg = new FileConfig(new File(dataFolder + "/languages", "messages_en.yml"));
            return;
        }
        langCfg = new FileConfig(new File(dataFolder + "/languages", "messages_" + plugin.getConfig().getString("language").toLowerCase() + ".yml"));
    }

    @Deprecated
    private void setupFile(File file, boolean replace) {
        plugin.getLogger().info("Loading " + file.getName() + ". Replaced: " + replace);
        if (!file.exists()) {
            try {
                file.createNewFile();
                InputStream is = plugin.getClass().getResourceAsStream("/" + file.getName());
                Files.copy(is, file.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create " + file.getName() + " file.");
            }
        }

        if (replace) {
            InputStream is = plugin.getClass().getResourceAsStream("/" + file.getName());
            try {
                Files.copy(is, file.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
