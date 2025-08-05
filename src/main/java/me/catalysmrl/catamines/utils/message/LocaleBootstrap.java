package me.catalysmrl.catamines.utils.message;

import me.catalysmrl.catamines.CataMines;

import java.nio.file.Path;

public class LocaleBootstrap {

    private final CataMines plugin;

    public LocaleBootstrap(CataMines plugin) {
        this.plugin = plugin;
    }

    public void init() {
        saveDefaultLang("messages_en.yml");

        Path langFolder = plugin.getDataPath().resolve("lang");
        LocaleManager localeManager = new LocaleManager(plugin);
        localeManager.loadLocales(langFolder);

        MessageService.init(localeManager);
    }

    private void saveDefaultLang(String fileName) {
        String resourcePath = "lang/" + fileName;
        plugin.saveResource(resourcePath, true);
    }
}
