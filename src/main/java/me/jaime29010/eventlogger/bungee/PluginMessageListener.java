package me.jaime29010.eventlogger.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import me.jaime29010.eventlogger.shared.BookData;
import me.jaime29010.eventlogger.shared.RenameData;
import me.jaime29010.eventlogger.shared.SignData;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PluginMessageListener implements Listener {
    private final Main main;
    public PluginMessageListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (event.getTag().equals("EventLogger") && event.getSender() instanceof Server) {
            ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
            String request = in.readUTF();

            ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();
            ServerInfo server = player.getServer().getInfo();

            switch (request) {
                case "LogSignEditEvent": {
                    String json = in.readUTF();
                    SignData data = main.getGson().fromJson(json, SignData.class);
                    String message = ChatColor.translateAlternateColorCodes('&',
                            main.getConfig().getString("SignEditMessage")
                                    .replace("%player%", player.getName())
                                    .replace("%server%", server.getName())
                                    .replace("%playerworld%", data.getLocation().getWorld())
                                    .replace("%playerxyz%", data.getLocation().toString())
                                    .replace("%line1%", data.getLines() [0])
                                    .replace("%line2%", data.getLines() [1])
                                    .replace("%line3%", data.getLines() [2])
                                    .replace("%line4%", data.getLines() [3]));
                    main.broadcast(new TextComponent(message));
                    main.log(ChatColor.stripColor(message));
                    break;
                }
                case "LogItemRenameEvent": {
                    String json = in.readUTF();
                    RenameData data = main.getGson().fromJson(json, RenameData.class);
                    String message = ChatColor.translateAlternateColorCodes('&',
                            main.getConfig().getString("ItemRenameMessage")
                                    .replace("%player%", player.getName())
                                    .replace("%server%", server.getName())
                                    .replace("%playerworld%", data.getLocation().getWorld())
                                    .replace("%playerxyz%", data.getLocation().toString())
                                    .replace("%type%", data.getTypeCaps())
                                    .replace("%name%", data.getName()));
                    main.broadcast(new TextComponent(message));
                    main.log(ChatColor.stripColor(message));
                    break;
                }
                case "LogBookEditEvent": {
                    String json = in.readUTF();
                    BookData data = main.getGson().fromJson(json, BookData.class);
                    String message = ChatColor.translateAlternateColorCodes('&',
                            main.getConfig().getString("BookEditMessage")
                                    .replace("%player%", player.getName())
                                    .replace("%server%", server.getName())
                                    .replace("%playerxyz%", data.getLocation().toString())
                                    .replace("%playerworld%", data.getLocation().getWorld())
                                    .replace("%chestxyz%", data.getChestLocation().toString())
                                    .replace("%chestworld%", data.getChestLocation().getWorld()));
                    main.broadcast(new TextComponent(message));
                    StringBuilder builder = new StringBuilder(ChatColor.stripColor(message));
                    int page = 1;
                    for (String string : data.getPages()) {
                        builder.append("\n");
                        builder.append("Page ");
                        builder.append(page);
                        builder.append("\n");
                        builder.append(ChatColor.stripColor(string));
                    }
                    main.log(builder.toString());
                    break;
                }
                case "BroadcastMessage": {
                    String message = in.readUTF();
                    main.broadcast(new TextComponent(ChatColor.translateAlternateColorCodes('&',
                            message)));
                    break;
                }
            }
        }
    }
}
