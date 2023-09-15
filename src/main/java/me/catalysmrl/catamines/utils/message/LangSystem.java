package me.catalysmrl.catamines.utils.message;

import me.catalysmrl.catamines.CataMines;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LangSystem {

    private final CataMines plugin;
    private final Path langDirectory;
    private final Path defaultDirectory;
    private final Path customDirectory;

    private Locale locale;
    private static FileConfiguration langFile;

    public LangSystem(CataMines plugin) {
        this.plugin = plugin;
        langDirectory = plugin.getDataFolder().toPath().resolve("lang");
        defaultDirectory = langDirectory.resolve("default");
        customDirectory = langDirectory.resolve("custom");
        reloadLang();
    }

    /**
     * Gets the desired translated message from a key. Messages are saved in yml files.
     * A static FileConfiguration file is loaded when plugin is enabled.
     * TODO: When translations are robust and reliable, consider using player specific locales.
     *
     * @param key the translation key
     * @return message retrieved from the key stored in a properties file
     */
    public static String getTranslatedMessage(String key) {
        if (langFile == null) return "Failed to load language file";
        return langFile.getString(key, "Missing " + key);
    }

    public static List<String> getTranslatedList(String key) {
        if (langFile == null) return Collections.singletonList("Failed to load language file");
        return langFile.contains(key) ? langFile.getStringList(key) : Collections.singletonList("Missing " + key);
    }

    public void reloadLang() {
        locale = new Locale(plugin.getConfig().getString("language", "en"));

        try {
            createDirectoriesIfNotExists(langDirectory);
            createDirectoriesIfNotExists(defaultDirectory);
            createDirectoriesIfNotExists(customDirectory);

            copyResourceToDisk("Lang_en.yml");
            copyResourceToDisk("Lang_de.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        langFile = resolveLangFile();
    }

    private void copyResourceToDisk(String resourceName) throws IOException {

        try (InputStream is = plugin.getResource("i18n/" + resourceName)) {
            if (is == null) {
                plugin.getLogger().severe("Could not find resource " + resourceName);
                return;
            }

            Files.copy(is, defaultDirectory.resolve(resourceName), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private FileConfiguration resolveLangFile() {
        String langFileName = "Lang_" + locale.toLanguageTag() + ".yml";

        Path customFile = customDirectory.resolve(langFileName);
        if (Files.exists(customFile)) {
            return YamlConfiguration.loadConfiguration(customFile.toFile());
        }

        Path defaultFile = defaultDirectory.resolve(langFileName);
        if (Files.exists(defaultFile)) {
            return YamlConfiguration.loadConfiguration(defaultFile.toFile());
        }

        plugin.getLogger().severe("Failed to load language file " + defaultFile);
        return null;
    }

    private static void createDirectoriesIfNotExists(Path path) throws IOException {
        if (Files.exists(path) && (Files.isDirectory(path) || Files.isSymbolicLink(path))) {
            return;
        }

        try {
            Files.createDirectories(path);
        } catch (FileAlreadyExistsException e) {
            // ignore
        }
    }
}