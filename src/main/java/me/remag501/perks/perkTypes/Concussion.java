package me.remag501.perks.perkTypes;

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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Concussion extends Perk implements Listener {

    // Track players who have the perk enabled
    private static final Map<UUID, Concussion> activePerks = new HashMap<>();

    public Concussion(ItemStack perkItem) {
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
    private void handleConcussionEffect(LivingEntity entity) {
        Player player = Bukkit.getPlayer(this.player);
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand.getType() == Material.AIR) {
            entity.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60, 0));
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
            Concussion perk = activePerks.get(damager.getUniqueId());
            if (perk == null) return; // Player does not have the perk enabled

            // Delegate behavior to the perk instance
            perk.handleConcussionEffect(entity);
        }
    }
}
