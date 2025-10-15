package me.remag501.perks.perktypes;

import me.remag501.perks.core.Perk;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class HotHandsPerk extends Perk {

    // Track players who have the perk enabled
    private static final Map<UUID, HotHandsPerk> activePerks = new HashMap<>();

    public HotHandsPerk(ItemStack perkItem) {
        super(perkItem);
//        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
//        player.sendMessage("Hot Hands perk activated!");
        // Register the perk's event listener
        activePerks.put(this.player, this);
//        player.getServer().getPluginManager().registerEvents(this, player.getServer().getPluginManager().getPlugin("Perks"));
    }

    @Override
    public void onDisable() {
//        player.sendMessage("Hot Hands perk disabled!");
        // Unregister the perk's event listener
//        HandlerList.unregisterAll(this);
        activePerks.remove(this.player);
    }

    // New encapsulated method to handle the perk effect
    private void handleHotHandsEffect(LivingEntity entity) {
        Player player = Bukkit.getPlayer(this.player);
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
            HotHandsPerk perk = activePerks.get(damager.getUniqueId());
            if (perk == null) return; // Player does not have the perk enabled

            // Delegate behavior to the perk instance
            perk.handleHotHandsEffect(entity);
        }
    }
}
