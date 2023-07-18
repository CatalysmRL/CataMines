package me.catalysmrl.catamines.utils.message;

import me.catalysmrl.catamines.CataMines;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

public class LangSystem {

    private final CataMines plugin;
    private final Path langDirectory;
    private final Path msgDirectory;
    private final Path guiDirectory;

    private static Locale locale;
    private static ResourceBundle bundle;

    private FileConfiguration guiLang;

    public LangSystem(CataMines plugin) {
        this.plugin = plugin;
        langDirectory = plugin.getDataFolder().toPath().toAbsolutePath().resolve("lang");
        msgDirectory = langDirectory.resolve("msg");
        guiDirectory = langDirectory.resolve("gui");
        reloadLang();
    }

    /**
     * Gets the desired translated message from a key. Messages are saved in properties
     * files. A resource bundle is responsible for handling i18n.
     *
     * @param key the translation key
     * @return message retrieved from the key stored in a properties file
     */
    public static String getTranslatedMessage(String key) {
        if (bundle == null) return "Resource bundle is null";
        try {
            return bundle.getString(key).replaceAll("%p%", Messages.PREFIX);
        } catch (MissingResourceException e) {
            return "Missing key: " + key;
        }
    }

    public static String getTranslatedGUIMessage(String key) {
        return null;
    }

    public void reloadLang() {
        locale = new Locale(plugin.getConfig().getString("language", "en"));

        try {
            createDirectoriesIfNotExists(langDirectory);
            createDirectoriesIfNotExists(msgDirectory);
            createDirectoriesIfNotExists(guiDirectory);

            copyMsgFile("Lang.properties");
            copyMsgFile("Lang_en.properties");
            copyMsgFile("Lang_de.properties");

            bundle = loadResourceBundle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyMsgFile(String resourceName) throws IOException {
        Path langPath = msgDirectory.resolve(resourceName);

        try (InputStream inputStream = new BufferedInputStream(Objects.requireNonNull(plugin.getResource("i18n/msg/" + resourceName)));
             OutputStream outputStream = Files.newOutputStream(langPath)) {
            inputStream.transferTo(outputStream);
        }

    }

    private ResourceBundle loadResourceBundle() throws IOException {
        URL[] urls = new URL[]{langDirectory.toUri().toURL()};
        try (URLClassLoader loader = new URLClassLoader(urls)) {
            return ResourceBundle.getBundle("Lang", locale, loader);
        }
    }

    private void loadGUILangFile() {
        String yamlFileName = "gui_" + locale.toLanguageTag() + ".yml";
        Path yamlFilePath = guiDirectory.resolve(yamlFileName);
        if (Files.exists(yamlFilePath)) {
            guiLang = YamlConfiguration.loadConfiguration(yamlFilePath.toFile());
        } else {
            plugin.getLogger().warning(yamlFilePath.toAbsolutePath() + " does not exist");
            guiLang = YamlConfiguration.loadConfiguration(guiDirectory.resolve("gui_en.yml").toFile());
        }
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