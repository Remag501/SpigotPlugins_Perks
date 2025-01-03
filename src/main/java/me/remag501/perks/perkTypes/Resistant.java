package me.remag501.perks.perkTypes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class Resistant extends Perk implements Listener {

    private static final double HEALTH_THRESHOLD = 0.25; // 25% health
    private BukkitTask healthCheckTask;
    private boolean isResistant;
    private int duration; // Store previous potion duration

    public Resistant(ItemStack perkItem) {
        super(perkItem);
    }

    @Override
    public void onEnable() {
        // Schedule a repeating task to check the player's health periodically
        healthCheckTask = Bukkit.getScheduler().runTaskTimer(
                player.getServer().getPluginManager().getPlugin("Perks"),
                () -> checkHealthAndApplyEffect(player),
                0L, 200L // Runs every ten seconds
        );
        isResistant = false;
        duration = 0;
        player.getServer().getPluginManager().registerEvents(this, player.getServer().getPluginManager().getPlugin("Perks"));
    }

    @Override
    public void onDisable() {
        // Cancel the repeating task when the perk is disabled
        if (healthCheckTask != null) {
            healthCheckTask.cancel();
        }
        isResistant = false;

        // Ensure to preserve any pre-existing Resistance effect
        PotionEffectType potion = PotionEffectType.DAMAGE_RESISTANCE;
        if (player.isOnline() && player.hasPotionEffect(potion) && player.getPotionEffect(potion).getAmplifier() == 0 && player.getPotionEffect(potion).getDuration() > 500) {
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        }

        EntityDamageEvent.getHandlerList().unregister(this);
        EntityRegainHealthEvent.getHandlerList().unregister(this);
    }

    // Periodically check player's health and apply the Resistance effect if needed
    private void checkHealthAndApplyEffect(Player player) {
        double currentHealth = player.getHealth();
        double maxHealth = player.getMaxHealth();

        if (currentHealth > 0 && currentHealth / maxHealth <= HEALTH_THRESHOLD) {
            if (!isResistant) {
                PotionEffect effect = player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                duration = 0;
                if (effect != null) {
                    if (effect.getAmplifier() > 0)
                        return;
                    duration = effect.getDuration();
                }
                isResistant = true;
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0)); // Infinite Resistance I
                player.sendMessage("You are now Resistant!");
            }
        } else {
            if (isResistant) {
                isResistant = false;
                PotionEffectType potion = PotionEffectType.DAMAGE_RESISTANCE;
                if (player.hasPotionEffect(potion) && player.getPotionEffect(potion).getAmplifier() == 0 && player.getPotionEffect(potion).getDuration() > 500) {
                    player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, 0));
                    duration = 0;
                }
                player.sendMessage("Your resistance has worn off.");
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

    @EventHandler
    public void onPlayerLoseEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if (event.getAction() == EntityPotionEffectEvent.Action.REMOVED && event.getModifiedType() == PotionEffectType.DAMAGE_RESISTANCE)
            checkHealthAndApplyEffect((Player) event.getEntity());
    }
}
