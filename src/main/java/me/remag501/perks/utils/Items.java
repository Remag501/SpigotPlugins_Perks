package me.remag501.perks.utils;

import me.remag501.perks.perkTypes.Perk;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Items {

    public static ItemStack createPerkItem(Perk perk) {
        // Returns an item depending on the perk name, Long Sword as placeholder

//        switch (perk) {
//            case PerkType.:
//                return createItem(Material.DIAMOND_SWORD, "Long Sword", true, "Here are some lores", "§4This is a powerful sword!");
//            default:
////                plugin.getLogger().info("Unknown perk: " + perkName);
//                return new ItemStack(Material.BEDROCK);
//        }
        return perk.getItem();
    }
    // Add identifier to this function arguments
    public static ItemStack createItem(Material type, String name, String id, boolean enchanted, String... lores) {
        // Function to make creating items easier

        // Example uses an IRON_SWORD as a placeholder item
        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();

        // Set the item's name and lore to describe the perk
        if (meta != null) {
            // Add lore to the item
            meta.setDisplayName(name);
            ArrayList<String> loreList = new ArrayList<>(Arrays.asList(lores));
            meta.setLore(loreList);
            // Add unique identifier to the item
            if (id != null) {
                NamespacedKey key = new NamespacedKey(Bukkit.getPluginManager().getPlugin("Perks"), "unique_id");
                PersistentDataContainer data = meta.getPersistentDataContainer();
                data.set(key, PersistentDataType.STRING, id);
            }
            // Make item look enchanted
            if (enchanted) {
                meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            // Apply meta deta to item
            item.setItemMeta(meta);
        }
        return item;
    }

    // Function to check if two ItemStacks are equal based on their PersistentDataContainer
    public static boolean areItemsEqual(ItemStack item1, ItemStack item2) {
        if (item1 == null || item2 == null) {
            return false; // One of the items is null, so they're not equal
        }

        // Get the ItemMeta for both items
        ItemMeta meta1 = item1.getItemMeta();
        ItemMeta meta2 = item2.getItemMeta();

        if (meta1 == null || meta2 == null) {
            return false; // One of the items has no metadata, so they're not equal
        }

        // Create a NamespacedKey for your custom identifier
        NamespacedKey key = new NamespacedKey(Bukkit.getPluginManager().getPlugin("Perks"), "unique_id");

        // Get the PersistentDataContainers from both items
        PersistentDataContainer data1 = meta1.getPersistentDataContainer();
        PersistentDataContainer data2 = meta2.getPersistentDataContainer();

        // Check if both items have the custom key in their PersistentDataContainer
        if (data1.has(key, PersistentDataType.STRING) && data2.has(key, PersistentDataType.STRING)) {
            String id1 = data1.get(key, PersistentDataType.STRING);
            String id2 = data2.get(key, PersistentDataType.STRING);

            // Compare the unique IDs
            return id1 != null && id1.equals(id2);
        }

        return false; // The custom data isn't present or doesn't match
    }

    public static void updateCount(ItemStack item, List<Perk> perks) {
        // Update the item's count based on the number of perks the player has
        int count = 0;
        for (Perk perk : perks) {
            if (areItemsEqual(item, perk.getItem()))
                count++;
        }
        // Check if meta is null
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return;
        // If the count is 0, then make the item a bedrock block
        if (count == 0) {
            item.setType(Material.BEDROCK);
            // Update meta data identifier
            NamespacedKey key = new NamespacedKey(Bukkit.getPluginManager().getPlugin("Perks"), "unique_id");
            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.remove(key);
        } else {
            // Update the item's count based on the number of perks the player has
            List<String> loreList = meta.getLore();
            if (meta.hasEnchants()) // Checks if selected
                loreList.add(1, "§7Perks: " + count + "/3");
            else
                loreList.add(0, "§7Perks: " + count + "/3");
            meta.setLore(loreList);
            // Revisit to find a way to keep remaining lore
        }
        item.setItemMeta(meta);
    }

    public static void updateEquipStatus(ItemStack item, List<Perk> equippedPerks) {
        // Check if perk is equipped
        boolean equipped = false;
        for (Perk perk: equippedPerks){
            if (areItemsEqual(item, perk.getItem())) {
                equipped = true;
                break;
            }
        }
        // Get the meta of the item
        ItemMeta meta = item.getItemMeta();
        // Enchant the item then add lore to show its equipped
        if (equipped) {
            // Enchant
            meta.addEnchant(Enchantment.LUCK, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            // Get previous lore
            List<String> lore = meta.getLore();
            if (lore == null)
                lore = new ArrayList<String>();
            lore.add(0, "§c§lEquipped");
            meta.setLore(lore);
        } else {
            meta.removeEnchant(Enchantment.LUCK);
            meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            // Get previous lore
            List<String> lore = meta.getLore();
            if (lore != null)
                lore.remove("§4Equipped");
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
    }

}
