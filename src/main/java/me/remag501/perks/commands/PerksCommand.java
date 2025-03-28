package me.remag501.perks.commands;

import me.remag501.perks.perkTypes.LongSwordPerk;
import me.remag501.perks.perkTypes.Perk;
import me.remag501.perks.perkTypes.PerkType;
import me.remag501.perks.perkTypes.PlayerPerks;
import me.remag501.perks.utils.Items;
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
            openPerkUI(sender, false);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                reload();
                return true;
            case "add":
                if (args.length == 1)
                    printPerks(sender);
                else if (args.length == 2)
                    addPerk((Player) sender, args[1]);
                else if (args.length == 3)
                    addPerk(sender, args[1], args[2]);
                else sender.sendMessage("Too many arguments");
                return true;
            case "addpoints":
                if (args.length == 1)
                    sender.sendMessage("Usage: /perks addpoints <player> <points>");
                else if (args.length == 2 && isNumeric(args[1]))
                    addPerkPoints(sender.getName(), Integer.parseInt(args[1]));
                else if (args.length == 3 && isNumeric(args[2]))
                    addPerkPoints(args[1], Integer.parseInt(args[2]));
                else if (args.length > 3)
                    sender.sendMessage("Too many arguments");
                return true;
            case "addcard":
                if (args.length == 1)
                    printPerks(sender);
                else if (args.length == 2)
                    addPerkCard(sender.getName(), args[1]);
                else if (args.length == 3)
                    addPerkCard(args[1], args[2]);
                else sender.sendMessage("Too many arguments");
                return true;
            case "remove":
                if (args.length == 1)
                    printPerks(sender);
                else if (args.length == 2)
                    removePerk(sender, sender.getName(), args[1]);
                else if (args.length == 3)
                    removePerk(sender, args[1], args[2]);
                else sender.sendMessage("Too many arguments");
                return true;
            case "hiddenui":
                openPerkUI(sender, true);
                return true;
            default:
                sender.sendMessage("Usage: reload/add/addpoints/addcard/remove");
                return true;
        }

    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private void printPerks(CommandSender sender) {
        sender.sendMessage("You need to specify a perk type");
        StringBuilder rv = new StringBuilder();
        for (PerkType type: PerkType.values()) {
            rv.append(String.valueOf(type)).append(" ");
        }
        sender.sendMessage(rv.toString());
    }

    private void addPerkCard(String playerName, String perkName) {

        // Get PlayerPerks object
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            Bukkit.getPluginManager().getPlugin("Perks").getLogger().info("Player " + playerName + " from add perk card could not be found.");
            return;
        }

        // Get perk from command arguments
        PerkType perkType;
        try {
            perkType = PerkType.valueOf(perkName);
        } catch (Exception e) {
            player.sendMessage("Invalid perk type: " + perkName);
            return;
        }

        // Get the perk card itemstack and give to player
        player.getInventory().addItem(Items.getPerkCard(perkType.getItem()));

    }

    private void addPerkPoints(String playerName, int points) {
        // Get PlayerPerks object
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            Bukkit.getPluginManager().getPlugin("Perks").getLogger().info("Player " + playerName + " from add perk points could not be founds.");
            return;
        }
        PlayerPerks playerPerks = PlayerPerks.getPlayerPerks(player.getUniqueId());
        playerPerks.addPerkPoints(points);
        player.sendMessage("You recieved " + points + " perk points.");
    }

    private void addPerk(Player player, String perkName) {
        // Get perk from command arguments
        PerkType perk;
        try {
            perk = PerkType.valueOf(perkName);
        } catch (Exception e) {
            player.sendMessage("Invalid perk type: " + perkName);
            return;
        }
        // Gets object of PlayerPerks from UUID
        PlayerPerks playerPerks = PlayerPerks.getPlayerPerks(player.getUniqueId());
//        if (playerPerks == null) {
//            playerPerks = new PlayerPerks(((Player) player).getUniqueId());
//        } Add perks should not instantinate PlayerPerks
        // Add perk to players owned perks list
        if(playerPerks.addOwnedPerks(perk))
            player.sendMessage("Added perk: " + perkName);
//        else
//            player.sendMessage("You have cannot have more than three perk cards");
    }

    private void addPerk(CommandSender sender, String playerName, String perkType) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage("Player not found: " + playerName);
            return;
        }
        try {PerkType.valueOf(perkType);}
        catch (Exception e) {
            sender.sendMessage("Invalid perk type: " + perkType);
            return;
        }
        addPerk(player, perkType);
    }

    private void removePerk(CommandSender sender, String playerName, String perkType) {
        // Get player from player name
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage("Player not found: " + playerName);
            return;
        }
        // Get perk from command arguments
        PerkType perk;
        try {
            perk = PerkType.valueOf(perkType);
        } catch (Exception e) {
            sender.sendMessage("Invalid perk type: " + perkType);
            return;
        }
        sender.sendMessage("Removed perk: " + perkType);
        // Gets object of PlayerPerks from UUID
        PlayerPerks playerPerks = PlayerPerks.getPlayerPerks(player.getUniqueId());
        if (playerPerks == null) {
            playerPerks = new PlayerPerks(player.getUniqueId());
        }
        // Remove perk from players owned perks list
        playerPerks.removeOwnedPerk(perk);
    }

    private void reload() {
        // Load from config file and player data
        // Perk data class describes what perks each player has available
//        PlayerPerks.savePerks();
    }

    private void openPerkUI(CommandSender sender, boolean hiddenMenu) {
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
        UI ui = new UI(PlayerPerks.getPlayerPerks(player.getUniqueId()), hiddenMenu);
        Inventory perkMenu = ui.getPerkMenu();
        player.openInventory(perkMenu);
    }

    public PerksCommand(Plugin plugin) {
        this.plugin = plugin;
    }

}
