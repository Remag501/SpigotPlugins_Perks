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

public class HotHandsPerk extends Perk implements Listener {

    public HotHandsPerk(ItemStack perkItem) {
        super(perkItem);
//        this.plugin = plugin;
    }

    @Override
    public void onEnable(Player player) {
//        player.sendMessage("Hot Hands perk activated!");
        // Register the perk's event listener
        player.getServer().getPluginManager().registerEvents(this, player.getServer().getPluginManager().getPlugin("Perks"));
    }

    @Override
    public void onDisable(Player player) {
//        player.sendMessage("Hot Hands perk disabled!");
        // Unregister the perk's event listener
        HandlerList.unregisterAll(this);
    }

    // Event listener to set entity on fire when punched with an empty hand
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Check if the damager is a player
        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity entity) {
            if (entity instanceof ArmorStand)
                return; // Ignore damage to armor stands
            Player player = (Player) event.getDamager();
            ItemStack itemInHand = player.getInventory().getItemInMainHand();

            // Check if the player is punching with an empty hand (no item)
            if (itemInHand == null || itemInHand.getType() == Material.AIR) {
                // Get the entity that was hit
                // Set the entity on fire for 5 seconds
                entity.setFireTicks(50); // 2.5 seconds
//                player.sendMessage("Hot Hands activated! You've set " + entity.getName() + " on fire!");
            }
        }
    }
}
