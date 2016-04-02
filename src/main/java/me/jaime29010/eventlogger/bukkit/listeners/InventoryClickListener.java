package me.jaime29010.eventlogger.bukkit.listeners;

import me.jaime29010.eventlogger.bukkit.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {
    private final Main main;
    public InventoryClickListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.isCancelled()) return;
        if (event.getClickedInventory() instanceof AnvilInventory && event.getSlot() == 2) {
            ItemStack item = event.getCurrentItem();
            if (item.getType() == Material.AIR) return;
            main.sendRenameEvent(player, item.getType().name(), item.getItemMeta().getDisplayName());
        }
    }
}
