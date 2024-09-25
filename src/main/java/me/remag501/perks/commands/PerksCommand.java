package me.remag501.perks.commands;

import me.remag501.perks.perkTypes.LongSwordPerk;
import me.remag501.perks.perkTypes.Perk;
import me.remag501.perks.perkTypes.PerkType;
import me.remag501.perks.perkTypes.PlayerPerks;
import me.remag501.perks.utils.UI;
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
            case "add":
                if (args.length == 1)
                    sender.sendMessage("You need to specify a perk type");
                else if (args.length > 2)
                    sender.sendMessage("Too many arguments");
                else
                    addPerk(sender, args[1]);
                return true;
            default:
                sender.sendMessage("Usage: awsjkflasdjf");
                return true;
        }

    }

    private void addPerk(CommandSender sender, String perkType) {
        // Get perk from command arguments
        PerkType perk;
        try {
            perk = PerkType.valueOf(perkType);
        } catch (Exception e) {
            sender.sendMessage("Invalid perk type: " + perkType);
            return;
        }
        sender.sendMessage("Added perk: " + perkType);
        // Gets object of PlayerPerks from UUID
        PlayerPerks playerPerks = PlayerPerks.getPlayerPerks(((Player) sender).getUniqueId());
        if (playerPerks == null) {
            playerPerks = new PlayerPerks(((Player) sender).getUniqueId());
        }
        // Add perk to players owned perks list
        playerPerks.addOwnedPerks(perk);
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
        // Open the inventory for the player
        PlayerPerks perks = PlayerPerks.getPlayerPerks(player.getUniqueId());
        if (perks == null) {
            perks = new PlayerPerks(player.getUniqueId());
        }
        UI ui = new UI(PlayerPerks.getPlayerPerks(player.getUniqueId()));
        Inventory perkMenu = ui.getPerkMenu();
        player.openInventory(perkMenu);
    }


    public PerksCommand(Plugin plugin) {
        this.plugin = plugin;
    }

}
