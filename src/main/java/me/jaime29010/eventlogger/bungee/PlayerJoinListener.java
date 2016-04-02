package me.jaime29010.eventlogger.bungee;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerJoinListener implements Listener {
    private final Main main;
    public PlayerJoinListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (player.hasPermission("signspy.notify")) {
            if (main.getStorage().containsKey(player.getUniqueId())) return;
            main.getStorage().put(player.getUniqueId(), true);
        }
    }
}
