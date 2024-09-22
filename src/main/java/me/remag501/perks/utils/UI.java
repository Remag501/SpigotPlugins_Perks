package me.remag501.perks.utils;

import me.remag501.perks.perkTypes.LongSwordPerk;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class UI {

    // Takes PlayerPerks as argument
    public static Inventory getPerkMenu() {
        Inventory perkInventory = Bukkit.createInventory(null, 9, "Choose Your Perk");

        // Get the player's PerkData
        // PerkData data = playerPerks.get(player.getUniqueId());

        // Add perk items to the inventory (example perks with placeholder items)
        // You'll replace the items with appropriate ones for each perk.
        int testLoop[] = new int[3];
//        for (Perk perk : data.getAvailablePerks()) {
        for (int i : testLoop) {
//            ItemStack perkItem = createPerkItem(perk);
            LongSwordPerk perk = new LongSwordPerk();
            ItemStack perkItem = Items.createPerkItem(perk);
            perkInventory.addItem(perkItem);
        }
        return perkInventory;
    }

}

// Add event handlers down here
