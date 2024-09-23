package me.remag501.perks.utils;

import me.remag501.perks.perkTypes.LongSwordPerk;
import me.remag501.perks.perkTypes.Perk;
import me.remag501.perks.perkTypes.PerkType;
import me.remag501.perks.perkTypes.PlayerPerks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class UI implements Listener {

    private PlayerPerks perks;
    private Inventory perkInventory;

    public UI(PlayerPerks perks) {
        this.perks = perks;
        perkInventory = Bukkit.createInventory(null, 54, "Choose Your Perk");
    }

    // Takes PlayerPerks as argument
    public Inventory getPerkMenu() {
        loadActivePerks();
        loadAvailablePerks( 0); // Default page is 0
        loadBackNextButton();

        return perkInventory;
    }

    private void loadActivePerks() {
        // Load active perks
        List<Perk> ownedPerks = perks.getOwnedPerks();
        int size = (ownedPerks == null) ? 0 : ownedPerks.size();
        for (int i = 0; i < 5; i++) {
            if (i < size) {
                ItemStack perkItem = Items.createPerkItem(ownedPerks.get(i));
                perkInventory.setItem(2 + i, perkItem);
            }
            else
                perkInventory.setItem(2 + i, new ItemStack(Material.AIR));
        }
    }

    private void loadAvailablePerks(int page) {
        // Load active perks
        for (int i = 19; i < 35; i++) {
            if (i % 9 == 0 || (i+1) % 9 == 0)
                continue;
            perkInventory.setItem(i, Items.createItem(Material.PAPER, "Page " + i, true)); // Add pages
        }
        perkInventory.setItem(19, PerkType.SWORD_PERK.getItem());
    }

    private void loadBackNextButton() {
        int page = 0, totalPages = 1;
//        int totalPages = Perk.perkAmount / 36;

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
        perks = PlayerPerks.getPlayerPerks(player.getUniqueId());

        // Check if the clicked inventory is the perk UI
        if (event.getView().getTitle().equals("Choose Your Perk")) {
            event.setCancelled(true); // Cancel the item removal
            if(event.getCurrentItem().equals(PerkType.SWORD_PERK.getItem())) {
                player.sendMessage("You clicked on the perk!");

                ClickType click = event.getClick();
                if (click == ClickType.LEFT)
                    perks.addPerk(PerkType.SWORD_PERK); // Create perk with player
                 else if (click == ClickType.RIGHT)
                    perks.removePerk(PerkType.SWORD_PERK); // Remove perk
                perkInventory = event.getInventory();
                loadActivePerks();
            }
            // You can also send a message to the player if needed
//            player.sendMessage("You clicked on the menu!");
        }
    }

}
