package me.catalysmrl.catamines.utils.message;

import me.catalysmrl.catamines.CataMines;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class LocaleManager {

    private final CataMines plugin;

    private final Map<Locale, YamlConfiguration> localeFiles = new HashMap<>();
    private final Locale defaultLocale = Locale.ENGLISH;

    public LocaleManager(CataMines plugin) {
        this.plugin = plugin;
    }

    public void loadLocales(Path folder) {
        localeFiles.clear();

        try {
            createDirectoriesIfNotExists(folder);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not create directories");
            return;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "messages_*.yml")) {
            for (Path path : stream) {
                Locale locale = parseLocale(path.getFileName().toString());
                YamlConfiguration config = YamlConfiguration.loadConfiguration(path.toFile());
                localeFiles.put(locale, config);
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to load locale files from: " + folder);
        }
    }

    public String getMessage(Locale locale, String key) {
        YamlConfiguration config = localeFiles.getOrDefault(locale, localeFiles.get(defaultLocale));
        String raw = config.getString(key, "§cMissing message: " + key);

        return raw.replace("%p", Messages.PREFIX);
    }

    public List<String> getList(Locale locale, String key) {
        YamlConfiguration config = localeFiles.getOrDefault(locale, localeFiles.get(defaultLocale));
        List<String> list = config.getStringList(key);

        if (list.isEmpty()) {
            return Collections.singletonList("§cMissing list: " + key);
        }

        return list.stream()
                .map(line -> line.replace("%p", Messages.PREFIX))
                .collect(Collectors.toList());
    }

    public String getFormatted(Locale locale, String key, Object... replacements) {
        String raw = getMessage(locale, key);
        for (int i = 0; i < replacements.length; i++) {
            raw = raw.replace("{" + i + "}", String.valueOf(replacements[i]));
        }
        return Messages.colorize(raw);
    }

    public List<String> getFormattedList(Locale locale, String key, Object... replacements) {
        List<String> rawList = getList(locale, key);
        List<String> result = new ArrayList<>();

        for (String line : rawList) {
            for (int i = 0; i < replacements.length; i++) {
                line = line.replace("{" + i + "}", String.valueOf(replacements[i]));
            }
            result.add(Messages.colorize(line));
        }

        return result;
    }

    public Locale getLocale(CommandSender sender) {
        if (sender instanceof Player player) {
            try {
                return player.locale();
            } catch (Exception e) {
                return defaultLocale;
            }
        }
        return defaultLocale;
    }

    private Locale parseLocale(String filename) {
        String code = filename.substring("messages_".length(), filename.indexOf('.'));
        return Locale.forLanguageTag(code);
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
