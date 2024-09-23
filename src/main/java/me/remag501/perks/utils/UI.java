package me.remag501.perks.utils;

import me.remag501.perks.perkTypes.LongSwordPerk;
import me.remag501.perks.perkTypes.Perk;
import me.remag501.perks.perkTypes.PlayerPerks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class UI implements Listener {

    // Takes PlayerPerks as argument
    public static Inventory getPerkMenu(PlayerPerks perks) {
        Inventory perkInventory = Bukkit.createInventory(null, 54, "Choose Your Perk");

        loadActivePerks(perkInventory, perks);
        loadAvailablePerks(perkInventory, 0); // Default page is 0
        loadBackNextButton(perkInventory);

        return perkInventory;
    }

    private static void loadActivePerks(Inventory perkInventory, PlayerPerks perks) {
        // Load active perks
        List<Perk> ownedPerks = perks.getOwnedPerks();
        int size = (ownedPerks == null) ? 0 : ownedPerks.size();
        for (int i = 0; i < size; i++) {
            ItemStack perkItem = Items.createPerkItem(ownedPerks.get(i));
            perkInventory.setItem(2 + i, perkItem); // Need to change order of items
        }
    }

    private static void loadAvailablePerks(Inventory perkInventory, int page) {
        // Load active perks
        for (int i = 19; i < 35; i++) {
            if (i % 9 == 0 || (i+1) % 9 == 0)
                continue;
            perkInventory.setItem(i, Items.createItem(Material.PAPER, "Page " + i, true)); // Add pages

        }
    }

    private static void loadBackNextButton(Inventory perkInventory) {
        int page = 0, totalPages = 1;
//        int totalPages = Perk.perkAmount / 36;
        loadAvailablePerks(perkInventory, page);

        if (page == totalPages - 1) // Last page
            perkInventory.setItem(53, Items.createItem(Material.STONE, "Last Page", false)); // No button needed
        else // Not last page
            perkInventory.setItem(53, Items.createItem(Material.GREEN_TERRACOTTA, "Next", false)); // Add next button
        if (page == 0) // First page
            perkInventory.setItem(45, Items.createItem(Material.STONE, "First Page", false)); // No button needed
        else // Not first page
            perkInventory.setItem(45, Items.createItem(Material.RED_TERRACOTTA, "Back", false)); // Add back button
    }

    // Handle inventory clicks
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        // Check if the clicked inventory is the perk UI
        if (event.getView().getTitle().equals("Choose Your Perk")) {
            event.setCancelled(true); // Cancel the item removal

            // You can also send a message to the player if needed
            player.sendMessage("You clicked on the menu!");
        }
    }

}
