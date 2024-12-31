package me.remag501.perks.perkTypes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class Bloodied extends Perk implements Listener {

    private static final double HEALTH_THRESHOLD = 0.25; // 25% health
    private BukkitTask healthCheckTask;
    private boolean isBloodied;
    private int duration;
    public Bloodied(ItemStack perkItem) {
        super(perkItem);
    }

    @Override
    public void onEnable(Player player) {
//        player.sendMessage("Bloodied Perk activated!");

        // Schedule a repeating task to check the player's health periodically
        healthCheckTask = Bukkit.getScheduler().runTaskTimer(
                player.getServer().getPluginManager().getPlugin("Perks"),
                () -> checkHealthAndApplyEffect(player),
                0L, 200L // Runs every ten seconds
        );
        isBloodied = false;
        duration = 0;
        player.getServer().getPluginManager().registerEvents(this, player.getServer().getPluginManager().getPlugin("Perks"));
    }

    @Override
    public void onDisable(Player player) {
//        player.sendMessage("Bloodied Perk deactivated!");

        // Cancel the repeating task when the perk is disabled
        if (healthCheckTask != null) {
            healthCheckTask.cancel();
        }
        isBloodied = false;
        // Make sure to remove the strength effect when disabling the perk
        PotionEffectType potion = PotionEffectType.INCREASE_DAMAGE;
        if (player.isOnline() && player.hasPotionEffect(potion)  && player.getPotionEffect(potion).getAmplifier() == 0 && player.getPotionEffect(potion).getDuration() > 500)
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        // Deregister the event
        EntityDamageEvent.getHandlerList().unregister(this);
        EntityRegainHealthEvent.getHandlerList().unregister(this);
    }

    // Periodically check player's health and apply the Strength effect if needed
    private void checkHealthAndApplyEffect(Player player) {
        double currentHealth = player.getHealth();
        double maxHealth = player.getMaxHealth();

        // If the player's health is 25% or below, give them Strength I
        if (currentHealth > 0 && currentHealth / maxHealth <= HEALTH_THRESHOLD) {
            if (!isBloodied) { // Prevents double messages
                isBloodied = true;
                duration = player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getDuration();
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0)); // Strength I for 2 seconds (40 ticks)
                player.sendMessage("You feel the strength of bloodied rage!");
            }
        } else {
            // Remove the strength effect if the player heals above 25%
            if (isBloodied) { // Prevents double messages
                isBloodied = false;
                PotionEffectType potion = PotionEffectType.INCREASE_DAMAGE;
                if (player.getPotionEffect(potion).getAmplifier() == 0 && player.getPotionEffect(potion).getDuration() > 500) {
                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, duration, 0));
                    duration = 0;
                }
                player.sendMessage("Your strength fades as you heal.");
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        checkHealthAndApplyEffect((Player) event.getEntity());
    }

    @EventHandler
    public void onPlayerHeal(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        checkHealthAndApplyEffect((Player) event.getEntity());
    }
}
