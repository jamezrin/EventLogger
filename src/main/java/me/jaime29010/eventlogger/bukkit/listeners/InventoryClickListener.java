package me.jaime29010.eventlogger.bukkit.listeners;

import me.jaime29010.eventlogger.bukkit.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
            ItemStack result = event.getCurrentItem();
            ItemStack item = event.getClickedInventory().getItem(0);
            if (result.getType() == Material.AIR) return;
            if (hasDifferentName(item, result)) {
                main.sendRenameEvent(player, result.getType().name(), result.getItemMeta().getDisplayName());
            }
        }
    }

    private boolean hasDifferentName(ItemStack item1, ItemStack item2) {
        String name1 = String.valueOf(getCustomName(item1));
        String name2 = String.valueOf(getCustomName(item2));
        return !name1.equals(name2);
    }

    private String getCustomName(ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            return item.getItemMeta().getDisplayName();
        }
        return null;
    }
}
