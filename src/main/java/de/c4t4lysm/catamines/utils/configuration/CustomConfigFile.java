package de.c4t4lysm.catamines.utils.configuration;

import de.c4t4lysm.catamines.CataMines;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;

public class CustomConfigFile extends YamlConfiguration {

    private final CataMines plugin;
    private final File file;


    public CustomConfigFile(CataMines plugin, File file, String resourceName) {

        this.plugin = plugin;
        this.file = file;

        try {

            if (!this.file.exists()) {
                this.file.getParentFile().mkdirs();
                this.file.createNewFile();
            }

            this.load(this.file);

            Map<String, Object> mappedConfig = this.getValues(true);
            try {
                Files.copy(plugin.getClass().getResourceAsStream("/" + resourceName), this.file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.load(this.file);

            for (Map.Entry<String, Object> entry : mappedConfig.entrySet()) {
                this.set(entry.getKey(), entry.getValue());
            }

            saveConfig();

        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        try {
            this.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
