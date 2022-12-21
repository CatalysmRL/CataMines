package me.catalysmrl.legacycatamines.utils.configuration;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigFile extends YamlConfiguration {
    private final File file;

    public ConfigFile(File file) {
        this.file = file;

        try {
            if (file.exists()) {
                load(file);
            } else {
                file.createNewFile();
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public ConfigFile(String path, String fileName) {
        this(new File(path, fileName));
    }

    public void saveConfig() {
        try {
            save(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void reload() {
        try {
            this.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }

}
