package me.remag501.perks.utils;

import me.remag501.perks.Perks;
import me.remag501.perks.perkTypes.PerkType;
import me.remag501.perks.perkTypes.PlayerPerks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GambleUI implements Listener {

//    private Player player;
    private Inventory rollInventory;
//    private PlayerPerks playerPerks;

    public GambleUI() {
//        this.player = player;
//        this.playerPerks = PlayerPerks.getPlayerPerks(player.getUniqueId());
        rollInventory = Bukkit.createInventory(null, 27, "Roll for Perks");
    }

    private void loadRollItems(Player player) {
//        for (int i = 0; i < 27; i++) {
//            rollInventory.setItem(i, new ItemStack(Material.PAPER)); // Example items for rolling
//        }
        PlayerPerks playerPerks = PlayerPerks.getPlayerPerks(player.getUniqueId());
        int perkPoints = playerPerks.getPerkPoints();
        ItemStack commonButton = Items.createItem(Material.WHITE_STAINED_GLASS_PANE, "COMMON", "common", true, "Costs 2 / " + perkPoints + " perk points");
        ItemStack uncommonButton = Items.createItem(Material.GREEN_STAINED_GLASS_PANE, "UNCOMMON", "uncommon", true, "Costs 4 / " + perkPoints + " perk points");
        ItemStack rareButton = Items.createItem(Material.BLUE_STAINED_GLASS_PANE, "RARE", "rare", true, "Costs 7 / " + perkPoints + " perk points");
        ItemStack legendaryButton = Items.createItem(Material.ORANGE_STAINED_GLASS_PANE, "LEGENDARY", "legendary", true, "Costs 10 / " + perkPoints + " perk points");
        // Set Locations
        rollInventory.setItem(10, commonButton);
        rollInventory.setItem(12, uncommonButton);
        rollInventory.setItem(14, rareButton);
        rollInventory.setItem(16, legendaryButton);
        // Add back arrow at slot 18
        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta meta = backArrow.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§c← Back");
            backArrow.setItemMeta(meta);
        }
        rollInventory.setItem(0, backArrow);
    }

    public void open(Player player) {
        loadRollItems(player);
        player.openInventory(rollInventory);
    }

    private void prevUI(Player player) {
        PlayerPerks perks = PlayerPerks.getPlayerPerks(player.getUniqueId());
        if (perks == null) {
            perks = new PlayerPerks(player.getUniqueId());
        }
        UI ui = new UI(PlayerPerks.getPlayerPerks(player.getUniqueId()), false);
        Inventory perkMenu = ui.getPerkMenu();
        player.openInventory(perkMenu);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Roll for Perks")) {
            event.setCancelled(true); // Prevent taking items

            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null) {
                if (clickedItem.getType() == Material.ARROW) {
                    prevUI(((Player) event.getWhoClicked()).getPlayer());
                    return;
                }
                switch (clickedItem.getItemMeta().getDisplayName()) {
                    case "COMMON":
                        rollPerk(player, 0, 2);
                        break;
                    case "UNCOMMON":
                        rollPerk(player, 1, 4);
                        break;
                    case "RARE":
                        rollPerk(player, 2, 7);
                        break;
                    case "LEGENDARY":
                        rollPerk(player, 3, 10);
                        break;
                    default:
                        break;
                }
                rollInventory = event.getInventory();
                loadRollItems(player); // Update UI
            }
        }
    }

    private boolean rollPerk(Player player, int rarity, int cost) {
        // Charge player perks, if it fails then it won't roll
        PlayerPerks playerPerks = PlayerPerks.getPlayerPerks(player.getUniqueId());
        if (!playerPerks.decreasePerkPoints(cost))
            return false; // Not enough points
        // Determine whether player rolls higher rarity
        int firstRoll = (int) (Math.random() * 100) + 1;
        if (firstRoll > 95)
            rarity += 2;
        else if (firstRoll > 80)
            rarity++;
        rarity = (rarity < 4) ? rarity : 3; // Floor rarity to 3
        // Find the possible perks
        List<PerkType> possiblePerks = PerkType.getPerksByRarity(rarity);
        // Roll random perk and add to user
        int randomIndex = (int) (Math.random() * possiblePerks.size());
        PerkType perkType = possiblePerks.get(randomIndex);
        playerPerks.addOwnedPerks(perkType);
        player.sendMessage("You obtained the perk: " + perkType.getItem().getItemMeta().getDisplayName());
        return true;
    }
}
