package me.remag501.perks.perkTypes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class Resistant extends Perk implements Listener {

    private BukkitTask healthCheckTask;

    public Resistant(ItemStack perkItem) {
        super(perkItem);
    }

    @Override
    public void onEnable(Player player) {
//        player.sendMessage("Resistant Perk activated!");

        // Register event listener
        Bukkit.getPluginManager().registerEvents(this, player.getServer().getPluginManager().getPlugin("Perks"));

        // Start a periodic task to check player's health and apply Resistance I if below 25%
        healthCheckTask = Bukkit.getScheduler().runTaskTimer(
                player.getServer().getPluginManager().getPlugin("Perks"),
                () -> checkHealthAndApplyResistance(player),
                0L, 20L // Run every second (20 ticks)
        );
    }

    @Override
    public void onDisable(Player player) {
//        player.sendMessage("Resistant Perk deactivated!");

        // Cancel the health check task when the perk is disabled
        if (healthCheckTask != null) {
            healthCheckTask.cancel();
        }

        // Unregister the listener
        EntityDamageEvent.getHandlerList().unregister(this);

        // Remove Resistance if still active
        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
    }

    // Method to check the player's health and apply Resistance I if under 25% HP
    private void checkHealthAndApplyResistance(Player player) {
        double currentHealth = player.getHealth();
        double maxHealth = player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue();

        if (currentHealth <= maxHealth * 0.25) { // If HP is 25% or less
            if (!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000000, 0)); // Resistance I
                player.sendMessage("You are now Resistant!");
            }
        } else {
            if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE); // Remove Resistance when HP is above 25%
                player.sendMessage("Your resistance has worn off.");
            }
        }
    }
}
