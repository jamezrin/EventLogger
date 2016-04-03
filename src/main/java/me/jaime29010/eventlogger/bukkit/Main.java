package me.jaime29010.eventlogger.bukkit;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import me.jaime29010.eventlogger.bukkit.listeners.InventoryClickListener;
import me.jaime29010.eventlogger.bukkit.listeners.PlayerEditBookListener;
import me.jaime29010.eventlogger.bukkit.listeners.SignChangeListener;
import me.jaime29010.eventlogger.shared.BookData;
import me.jaime29010.eventlogger.shared.LocationData;
import me.jaime29010.eventlogger.shared.RenameData;
import me.jaime29010.eventlogger.shared.SignData;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private final Gson gson = new Gson();
    private Inventory storage;
    private Location location;
    private FileConfiguration config;
    @Override
    public void onEnable() {
        config = ConfigurationManager.loadConfig("bukkitconfig.yml", this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "EventLogger");

        location = new Location(
                getServer().getWorld(config.getString("Storage.world")),
                config.getInt("Storage.x"),
                config.getInt("Storage.y"),
                config.getInt("Storage.z"));
        Block block = location.getBlock();
        if (block.getState() instanceof Chest) {
            Chest chest = (Chest) block.getState();
            storage = chest.getInventory();
        } else {
            getLogger().severe("The location specified in the config is not a chest, we will not store the copies of the books");
        }

        getCommand("signspy").setExecutor(new SignSpyExecutor(this));
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new SignChangeListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerEditBookListener(this), this);
    }

    public Inventory getStorage() {
        return storage;
    }

    @Override
    public FileConfiguration getConfig() {
        return config;
    }

    public Gson getGson() {
        return gson;
    }

    public TextComponent getConfigMessage(String path) {
        return new TextComponent(ChatColor.translateAlternateColorCodes('&',
                config.getString(path)));
    }

    public void sendSignEvent(Player player, String[] lines) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("LogSignEditEvent");
        String json = gson.toJson(new SignData(new LocationData(
                player.getWorld().getName(),
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ()
        ), lines));
        out.writeUTF(json);
        player.sendPluginMessage(this, "EventLogger", out.toByteArray());
    }

    public void sendRenameEvent(Player player, String type, String name) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("LogItemRenameEvent");
        String json = gson.toJson(new RenameData(new LocationData(
                player.getWorld().getName(),
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ()
        ), type, name));
        out.writeUTF(json);
        player.sendPluginMessage(this, "EventLogger", out.toByteArray());
    }

    public void sendBookEvent(Player player, BookMeta meta) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("LogBookEditEvent");
        String json = gson.toJson(new BookData(new LocationData(
                player.getWorld().getName(),
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ()
        ), meta.getTitle(), meta.getAuthor(), meta.getPages(), new LocationData(
                location.getWorld().getName(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ())));
        out.writeUTF(json);
        player.sendPluginMessage(this, "EventLogger", out.toByteArray());
    }
}