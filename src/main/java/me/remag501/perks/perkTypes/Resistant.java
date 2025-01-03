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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Resistant extends Perk implements Listener {

    private static final double HEALTH_THRESHOLD = 0.25; // 25% health
    private static final Map<UUID, Resistant> activePerks = new HashMap<>();

    private BukkitTask healthCheckTask;
    private boolean isResistant;
    private int duration; // Store previous potion duration

    public Resistant(ItemStack perkItem) {
        super(perkItem);
    }

    @Override
    public void onEnable() {
        Player player = Bukkit.getPlayer(this.player);
        activePerks.put(this.player, this);

        healthCheckTask = Bukkit.getScheduler().runTaskTimer(
                player.getServer().getPluginManager().getPlugin("Perks"),
                () -> checkHealthAndApplyEffect(player),
                0L, 200L // Runs every ten seconds
        );

        isResistant = false;
        duration = 0;
    }

    @Override
    public void onDisable() {
        Player player = Bukkit.getPlayer(this.player);
        activePerks.remove(this.player);

        if (healthCheckTask != null) {
            healthCheckTask.cancel();
        }

        isResistant = false;

        PotionEffectType potion = PotionEffectType.DAMAGE_RESISTANCE;
        if (player.isOnline() && player.hasPotionEffect(potion)
                && player.getPotionEffect(potion).getAmplifier() == 0
                && player.getPotionEffect(potion).getDuration() > 500) {
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        }
    }

    private void checkHealthAndApplyEffect(Player player) {
        double currentHealth = player.getHealth();
        double maxHealth = player.getMaxHealth();

        if (currentHealth > 0 && currentHealth / maxHealth <= HEALTH_THRESHOLD) {
            if (!isResistant) {
                PotionEffect effect = player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                duration = 0;

                if (effect != null) {
                    if (effect.getAmplifier() > 0)
                        return; // Don't override stronger resistance effects
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

    // Static method to handle external disable calls
    public static void handlePlayerDisable(Player player) {
        Resistant perk = activePerks.get(player);
        if (perk != null) {
            perk.onDisable();
        }
    }

    public static boolean isActive(Player player) {
        return activePerks.containsKey(player);
    }

    // Event Handlers
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (isActive(player)) {
            activePerks.get(player.getUniqueId()).checkHealthAndApplyEffect(player);
        }
    }

    @EventHandler
    public void onPlayerHeal(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (isActive(player)) {
            activePerks.get(player.getUniqueId()).checkHealthAndApplyEffect(player);
        }
    }

    @EventHandler
    public void onPlayerLoseEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;

        if (isActive(player)) {
            activePerks.get(player.getUniqueId()).checkHealthAndApplyEffect(player);
        }
    }
}
