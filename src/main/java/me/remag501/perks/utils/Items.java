package me.remag501.perks.utils;

import me.remag501.perks.perkTypes.Perk;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

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

    public static ItemStack createItem(Material type, String name, boolean enchanted, String... lores) {
        // Function to make creating items easier

        // Example uses an IRON_SWORD as a placeholder item
        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();

        // Set the item's name and lore to describe the perk
        if (meta != null) {
            meta.setDisplayName(name);
            ArrayList<String> loreList = new ArrayList<>(Arrays.asList(lores));
            meta.setLore(loreList);
            if (enchanted) {
                meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            item.setItemMeta(meta);
        }
        return item;
    }


}
