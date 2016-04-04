package me.jaime29010.eventlogger.bukkit.listeners;

import me.jaime29010.eventlogger.bukkit.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class PlayerEditBookListener implements Listener {
    private final Main main;
    public PlayerEditBookListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerEditBook(PlayerEditBookEvent event) {
        Player player = event.getPlayer();
        //Saving the book to the storage
        BookMeta meta = event.getNewBookMeta();
        if (main.getStorage() != null) {
            if (meta.hasPages()) {
                ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
                if (!meta.hasAuthor()) {
                    meta.setAuthor(player.getName());
                }
                if (!meta.hasTitle()) {
                    meta.setTitle("Unsigned Book");
                }
                item.setItemMeta(meta);
                main.getStorage().getInventory().addItem(item);
            }
        }
        main.sendBookEvent(player, meta);
    }
}