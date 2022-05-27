package de.c4t4lysm.catamines.utils;

import de.c4t4lysm.catamines.CataMines;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileConfig extends YamlConfiguration {

    private File file;

    public FileConfig(File file) {
        this.file = file;

        try {
            this.file.createNewFile();
            this.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public FileConfig(String filename) {
        String path = CataMines.getInstance().getDataFolder() + "/" + filename;
        this.file = new File(path);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        try {
            this.load(file);
        } catch (InvalidConfigurationException | IOException ex) {
            ex.printStackTrace();
        }

    }

    public FileConfig(String path, String fileName) {
        String finalPath = path + "/" + fileName;
        File directory = new File(path);

        if (!directory.exists() && !directory.mkdirs()) {
            return;
        }

        this.file = new File(finalPath);
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            try {
                this.load(this.file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            save(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void reloadConfig() {
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
