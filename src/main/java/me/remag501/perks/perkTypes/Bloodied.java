package me.remag501.perks.perkTypes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class Bloodied extends Perk {

    private static final double HEALTH_THRESHOLD = 0.25; // 25% health
    private BukkitTask healthCheckTask;

    public Bloodied(ItemStack perkItem) {
        super(perkItem);
    }

    @Override
    public void onEnable(Player player) {
        player.sendMessage("Bloodied Perk activated!");

        // Schedule a repeating task to check the player's health periodically
        healthCheckTask = Bukkit.getScheduler().runTaskTimer(
                player.getServer().getPluginManager().getPlugin("Perks"),
                () -> checkHealthAndApplyEffect(player),
                0L, 20L // Runs every 20 ticks (1 second)
        );
    }

    @Override
    public void onDisable(Player player) {
        player.sendMessage("Bloodied Perk deactivated!");

        // Cancel the repeating task when the perk is disabled
        if (healthCheckTask != null) {
            healthCheckTask.cancel();
        }

        // Make sure to remove the strength effect when disabling the perk
        player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
    }

    // Periodically check player's health and apply the Strength effect if needed
    private void checkHealthAndApplyEffect(Player player) {
        if (player.isOnline()) {
            double currentHealth = player.getHealth();
            double maxHealth = player.getMaxHealth();

            // If the player's health is 25% or below, give them Strength I
            if (currentHealth > 0 && currentHealth / maxHealth <= HEALTH_THRESHOLD) {
                if (!player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, 0)); // Strength I for 2 seconds (40 ticks)
                    player.sendMessage("You feel the strength of bloodied rage!");
                }
            } else {
                // Remove the strength effect if the player heals above 25%
                if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                    player.sendMessage("Your strength fades as you heal.");
                }
            }
        }
    }
}
