package me.jaime29010.eventlogger.bungee;

import com.google.gson.Gson;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Main extends Plugin {
    private Logger logger;
    private final Gson gson = new Gson();
    private static final int MAX_SIZE_MB = 5;
    private static final int MAX_LOG_FILES = 10;
    private final Map<UUID, Boolean> storage = new HashMap<>();
    private Configuration config;
    @Override
    public void onEnable() {
        config = ConfigurationManager.loadConfig("bungeeconfig.yml", this);
        getProxy().registerChannel("EventLogger");

        getProxy().getPluginManager().registerListener(this, new PlayerJoinListener(this));
        getProxy().getPluginManager().registerListener(this, new PluginMessageListener(this));
        getProxy().getPluginManager().registerCommand(this, new SignSpyCommand(this));
        logger = Logger.getLogger("Event Log");
        logger.setUseParentHandlers(false);
        try {
            FileHandler handler = new FileHandler(
                    new File(getDataFolder(), "events.log").getAbsolutePath(),
                    MAX_SIZE_MB * 1000000,
                    MAX_LOG_FILES);
            Formatter formatter = new Formatter() {
                @Override
                public String format(LogRecord record) {
                    return String.format("[%s] %s\n", new Date(record.getMillis()).toString(), record.getMessage());
                }
            };
            handler.setFormatter(formatter);
            logger.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Gson getGson() {
        return gson;
    }

    public Configuration getConfig() {
        return config;
    }

    public void broadcast(String message) {
        for (Map.Entry<UUID, Boolean> entry : storage.entrySet()) {
            if (entry.getValue()) {
                ProxiedPlayer player = getProxy().getPlayer(entry.getKey());
                if (player == null) return;
                player.sendMessage(new TextComponent(message));
            }
        }
    }

    public TextComponent getConfigMessage(String path) {
        return new TextComponent(color(config.getString(path)));
    }

    public String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public void reloadConfig() {
        config = ConfigurationManager.loadConfig("bungeeconfig.yml", this);
    }

    public void log(String message) {
        logger.info(message);
    }

    public Map<UUID, Boolean> getStorage() {
        return storage;
    }
}
