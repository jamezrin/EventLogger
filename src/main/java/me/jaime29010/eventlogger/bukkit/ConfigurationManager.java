package me.jaime29010.eventlogger.bukkit;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ConfigurationManager {
    public static FileConfiguration loadConfig(File file, Main main) {
        FileConfiguration config = null;
        if (!main.getDataFolder().exists())
            main.getDataFolder().mkdir();
        if (!file.exists()) {
            try {
                Files.copy(main.getResource(file.getName()), file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
        return config;
    }
    public static FileConfiguration loadConfig(String name, Main main) {
        return loadConfig(new File(main.getDataFolder(), name), main);
    }

    public static boolean saveConfig(FileConfiguration config, File file, Main main) {
        if (!main.getDataFolder().exists())
            main.getDataFolder().mkdir();
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean saveConfig(FileConfiguration config, String name, Main main) {
        return saveConfig(config, new File(main.getDataFolder(), name), main);
    }
}