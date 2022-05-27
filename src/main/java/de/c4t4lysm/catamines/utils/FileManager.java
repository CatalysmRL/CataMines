package de.c4t4lysm.catamines.utils;

import de.c4t4lysm.catamines.CataMines;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileManager {

    private final CataMines plugin;
    private final File dataFolder;
    private FileConfig langCfg;
    private FileConfig propertiesCfg;

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
            return "Could not load " + str + ", language you're using: " + plugin.getConfig().getString("language");
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
            return Collections.singletonList("Could not load " + str + ", language you're using: " + plugin.getConfig().getString("language"));
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
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();

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

        this.propertiesCfg = new FileConfig(new File(dataFolder, "default_properties.yml"));
        setupCustomFile(propertiesCfg, "default_properties.yml");


    }

    public void setupLanguageFiles() {

        setupCustomFile(new FileConfig(new File(dataFolder + "/languages", "messages_en.yml")), "messages_en.yml");
        setupCustomFile(new FileConfig(new File(dataFolder + "/languages", "messages_custom.yml")), "messages_custom.yml");
        setupCustomFile(new FileConfig(new File(dataFolder + "/languages", "messages_es.yml")), "messages_es.yml");

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

    private void setupCustomFile(FileConfig customConfig, String resourceName) {

        InputStreamReader defConfigStream = new InputStreamReader(plugin.getResource(resourceName), StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        customConfig.setDefaults(defConfig);
        customConfig.reloadConfig();
        customConfig.saveConfig();
    }

}
