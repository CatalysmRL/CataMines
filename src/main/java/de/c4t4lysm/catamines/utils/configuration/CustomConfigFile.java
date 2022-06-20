package de.c4t4lysm.catamines.utils.configuration;

import de.c4t4lysm.catamines.CataMines;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Objects;

public class CustomConfigFile extends YamlConfiguration {

    private final File file;

    public CustomConfigFile(CataMines plugin, File file, String resourceName) {

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

            Map<String, Object> defaultMap = YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getClass().getResourceAsStream("/" + resourceName))).getValues(true);
            for (Map.Entry<String, Object> entry : defaultMap.entrySet()) {
                if (this.contains(entry.getKey())) {
                    continue;
                }

                this.set(entry.getKey(), entry.getValue());
            }

            saveConfig();

        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public CustomConfigFile(CataMines plugin, File file, String resourceName, StandardCopyOption copyOption) {

        this.file = file;

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            InputStream is = plugin.getClass().getResourceAsStream("/" + resourceName);
            load(new InputStreamReader(Objects.requireNonNull(is)));

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
