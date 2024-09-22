package me.remag501.perks.commands;

import me.remag501.perks.perkTypes.LongSwordPerk;
import me.remag501.perks.perkTypes.Perk;
import me.remag501.perks.perkTypes.PlayerPerks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class PerksCommand implements CommandExecutor {

    private Plugin plugin;
    private Map<String, String> messages;
    private Map<UUID, PlayerPerks> playerPerks;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players.");
            return true;
        }
        if (args.length == 0) {
            openPerkUI(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                reload();
                return true;
            default:
                sender.sendMessage("Usage: awsjkflasdjf");
                return true;
        }

    }

    private void reload() {
        // Load from config file and player data
        // Perk data class describes what perks each player has available
    }

    private void openPerkUI(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return;
        }

        Player player = (Player) sender;
        // Create a custom inventory with 9 slots
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
            ItemStack perkItem = createPerkItem(perk);
            perkInventory.addItem(perkItem);
        }

        // Open the inventory for the player
        player.openInventory(perkInventory);
    }

//    private ItemStack createPerkItem(Perk perk) {
    private ItemStack createPerkItem(Perk perk) {
        // Returns an item depending on the perk name, Long Sword as placeholder
        String perkName = perk.getName();

        switch (perkName) {
            case "Long Sword":
                return createItem(Material.DIAMOND_SWORD, "Long Sword", true, "Here are some lores", "§4This is a powerful sword!");
            default:
                plugin.getLogger().info("Unknown perk: " + perkName);
                return new ItemStack(Material.BEDROCK);
        }

    }

    private ItemStack createItem(Material type, String name, boolean enchanted, String... lores) {
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


    public PerksCommand(Plugin plugin) {
        this.plugin = plugin;
    }

}
