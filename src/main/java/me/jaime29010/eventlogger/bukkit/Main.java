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
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private final Gson gson = new Gson();
    private Chest storage;
    @Override
    public void onEnable() {
        FileConfiguration config = ConfigurationManager.loadConfig("bukkitconfig.yml", this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "EventLogger");
        ConfigurationSection section = config.getConfigurationSection("Storage");
        World world = getServer().getWorld(section.getString("world"));
        if (world != null) {
            int x = section.getInt("x");
            int y = section.getInt("y");
            int z = section.getInt("z");

            //Not sure if needed?
            Chunk chunk = world.getChunkAt(x, z);
            chunk.load();

            Block block = world.getBlockAt(x, y, z);
            if (block != null && block.getState() instanceof Chest) {
                storage = (Chest) block.getState();
            } else {
                getLogger().severe("The location specified in the config is not a chest, we will not store the copies of the books");
            }
        } else {
            getLogger().severe("The world specified in the config does not exist, we will not store the copies of the books");
        }
        getCommand("eventlogger").setExecutor(new EventLoggerExecutor(this));
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new SignChangeListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerEditBookListener(this), this);
    }

    public Chest getStorage() {
        return storage;
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
        ), meta.getTitle(), meta.getAuthor(), meta.getPages(), storage != null ? new LocationData(
                storage.getWorld().getName(),
                storage.getLocation().getBlockX(),
                storage.getLocation().getBlockY(),
                storage.getLocation().getBlockZ()) : null));
        out.writeUTF(json);
        player.sendPluginMessage(this, "EventLogger", out.toByteArray());
    }
}