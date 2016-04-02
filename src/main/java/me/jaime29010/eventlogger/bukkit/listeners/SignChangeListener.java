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
        main.sendSignEvent(player, event.getLines());
    }
}
