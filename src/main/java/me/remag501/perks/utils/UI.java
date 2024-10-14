package me.remag501.perks.utils;

import me.remag501.perks.perkTypes.Perk;
import me.remag501.perks.perkTypes.PerkType;
import me.remag501.perks.perkTypes.PlayerPerks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class UI implements Listener {

    private PlayerPerks perks;
    private Inventory perkInventory;
    private boolean hiddenMenu;

    public UI(PlayerPerks perks, boolean hiddenMenu) {
        this.hiddenMenu = hiddenMenu;
        this.perks = perks;
        perkInventory = Bukkit.createInventory(null, 54, "Choose Your Perk");
    }

    // Takes PlayerPerks as argument
    public Inventory getPerkMenu() {
        loadActivePerks();
        loadAvailablePerks(0); // Default page is 0
        loadBackNextButton();

        return perkInventory;
    }

    private void loadActivePerks() {
        // Load active perks
        List<Perk> equippedPerks = perks.getEquippedPerks();
        int size = (equippedPerks == null) ? 0 : equippedPerks.size();
        for (int i = 0; i < 5; i++) {
            if (i < size) {
//                ItemStack perkItem = Items.createPerkItem(equippedPerks.get(i));
                ItemStack perkItem = equippedPerks.get(i).getItem();
                perkInventory.setItem(2 + i, perkItem);
            } else
                perkInventory.setItem(2 + i, new ItemStack(Material.AIR));
        }
    }

    private void loadAvailablePerks(int page) {
        // Load owned perks
        List<Perk> ownedPerks = perks.getOwnedPerks();
        List<Perk> equippedPerks = perks.getEquippedPerks();
        for (int i = 19, size = PerkType.values().length, k = 0; i < 35; i++) {
            if (i % 9 == 0 || (i + 1) % 9 == 0)
                continue;
            if (k < size) {
                ItemStack item = PerkType.values()[k].getItem().clone(); // Prevent mutilating enum object
                k++;
                // Checks if the item is hidden
                if (Items.hiddenItem(item) && !hiddenMenu) {
                    i--; // Don't move to next UI slot
                    continue;
                }
                // Display the perk item
                Items.updateEquipStatus(item, equippedPerks);
                Items.updateCount(item, ownedPerks);
                perkInventory.setItem(i, item);
            } else
                perkInventory.setItem(i, Items.createItem(Material.BARRIER, "???", null, true));
        }
//        ItemStack perkItem = PerkType.SWORD_PERK.getItem().clone();
//        ItemMeta meta = perkItem.getItemMeta().clone();
//        ArrayList lore = new ArrayList();
//        lore.add("x/3 owned");
//        meta.setLore(lore);
//        perkItem.setItemMeta(meta);
//        perkInventory.setItem(19, perkItem);
    }

    private void loadBackNextButton() {
        int page = 0, totalPages = 1;
//        int totalPages = Perk.perkAmount / 36;

        if (page == totalPages - 1) // Last page
            perkInventory.setItem(53, Items.createItem(Material.STONE, "Last Page", null, false)); // No button needed
        else // Not last page
            perkInventory.setItem(53, Items.createItem(Material.GREEN_CONCRETE, "Next", null, false)); // Add next button
        if (page == 0) // First page
            perkInventory.setItem(45, Items.createItem(Material.STONE, "First Page", null, false)); // No button needed
        else // Not first page
            perkInventory.setItem(45, Items.createItem(Material.RED_CONCRETE, "Back", null, false)); // Add back button

    }

    // Handle inventory clicks
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        perks = PlayerPerks.getPlayerPerks(player.getUniqueId());

        // Check if the clicked inventory is the perk UI
        if (event.getView().getTitle().equals("Choose Your Perk")) {
            event.setCancelled(true); // Cancel the item removal

            NamespacedKey key = new NamespacedKey(Bukkit.getPluginManager().getPlugin("Perks"), "unique_id");
            for (PerkType perkType : PerkType.values()) {
                if (Items.areItemsEqual(event.getCurrentItem(), perkType.getItem())) {
                    // Check if the perk event is triggered by hidden item, if so make this object have hidden perks
                    if (Items.hiddenItem(event.getCurrentItem()))
                        hiddenMenu = true;
//                    Bukkit.getPluginManager().getPlugin("Perks").getLogger().info(String.valueOf(perkType.getItem().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING)));
                    // Add or remove the perk from player's equipped perks based on the click type (left or right)'
                    ClickType click = event.getClick();
                    if (click == ClickType.LEFT)
                        perks.addEquippedPerk(perkType);
                    else if (click == ClickType.RIGHT)
                        perks.removeEquippedPerk(perkType);
                    perkInventory = event.getInventory();
                    loadActivePerks();
                    loadAvailablePerks(0);
                }
            }
        }
    }

}
