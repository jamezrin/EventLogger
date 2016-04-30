package me.jaime29010.eventlogger.bukkit.listeners;

import me.jaime29010.eventlogger.bukkit.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {
    private final Main main;
    public SignChangeListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (main.getBukkitConfig().getBoolean("CheckEmptySigns")) {
            if (hasText(event.getLines())) {
                main.sendSignEvent(player, event.getLines());
            }
        } else {
            main.sendSignEvent(player, event.getLines());
        }
    }

    private boolean hasText(String[] lines) {
        return (lines[0].matches(".*\\w.*") ||
                lines[1].matches(".*\\w.*") ||
                lines[2].matches(".*\\w.*") ||
                lines[3].matches(".*\\w.*"));
    }
}
