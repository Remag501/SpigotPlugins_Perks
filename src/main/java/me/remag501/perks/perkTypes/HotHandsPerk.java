package me.remag501.perks.perkTypes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.HandlerList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HotHandsPerk extends Perk implements Listener {

    // Track players who have the perk enabled
    private static final Map<Player, HotHandsPerk> activePerks = new HashMap<>();
    private String testName;

    public HotHandsPerk(ItemStack perkItem) {
        super(perkItem);
//        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
//        player.sendMessage("Hot Hands perk activated!");
        // Register the perk's event listener
        testName = player.getName();
        activePerks.put(player, this);
//        player.getServer().getPluginManager().registerEvents(this, player.getServer().getPluginManager().getPlugin("Perks"));
    }

    @Override
    public void onDisable() {
//        player.sendMessage("Hot Hands perk disabled!");
        // Unregister the perk's event listener
//        HandlerList.unregisterAll(this);
        activePerks.remove(player);
    }

    // New encapsulated method to handle the perk effect
    private void handleHotHandsEffect(LivingEntity entity) {
        player.sendMessage("Test name var: " + testName); // Use instance variable internally
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand.getType() == Material.AIR) {
            entity.setFireTicks(50); // 2.5 seconds
        }
    }

    // Event listener to set entity on fire when punched with an empty hand
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Check if the damager is a player
        if (event.getDamager() instanceof Player damager && event.getEntity() instanceof LivingEntity entity) {
            if (entity instanceof ArmorStand)
                return; // Ignore damage to armor stands

            // Check if the player has the perk enabled
            HotHandsPerk perk = activePerks.get(damager);
            if (perk == null) return; // Player does not have the perk enabled

            // Delegate behavior to the perk instance
            perk.handleHotHandsEffect(entity);
        }
    }
}
