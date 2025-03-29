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
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Bloodied extends Perk implements Listener {

    private double healthThreshold; // Varies by stars acquired
    private int amplifier;
    private static final Map<UUID, Bloodied> activePerks = new HashMap<>();

    private BukkitTask healthCheckTask;
    private boolean isBloodied;
    private int duration;
    private int kit_amplifier;

    public Bloodied(ItemStack perkItem, boolean starPerk) {
        super(perkItem, starPerk);
    }

    @Override
    public void onEnable() {
        Player player = Bukkit.getPlayer(this.player);
        activePerks.put(this.player, this);

        // Schedule a repeating task to check the player's health periodically
        healthCheckTask = Bukkit.getScheduler().runTaskTimer(
                player.getServer().getPluginManager().getPlugin("Perks"),
                () -> checkHealthAndApplyEffect(player),
                0L, 200L // Runs every 10 seconds
        );

        isBloodied = false;
        duration = 0;
        kit_amplifier = 0;
        switch(super.getStars()) {
            case 1:
                healthThreshold = 0.2;
                amplifier = 0;
                break;
            case 2:
                healthThreshold = 0.3;
                amplifier = 0;
                break;
            case 3:
                healthThreshold = 0.4;
                amplifier = 0;
                break;

        }
    }

    @Override
    public void onDisable() {
        Player player = Bukkit.getPlayer(this.player);
        activePerks.remove(this.player);
        if (healthCheckTask != null) {
            healthCheckTask.cancel();
        }
        PotionEffectType potion = PotionEffectType.INCREASE_DAMAGE;

        if (player.isOnline() && player.hasPotionEffect(potion)
                && player.getPotionEffect(potion).getAmplifier() == amplifier
                && player.getPotionEffect(potion).getDuration() > 500) {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }

    }

    private void checkHealthAndApplyEffect(Player player) {
        double currentHealth = player.getHealth();
        double maxHealth = player.getMaxHealth();

        if (currentHealth > 0 && currentHealth / maxHealth <= healthThreshold) {
            if (!isBloodied) {
                PotionEffect effect = player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE);
                duration = 0;

                if (effect != null) {
                    if (effect.getAmplifier() > amplifier)
                        return; // Don't apply bloodied effects if the user already has strength
                    duration = effect.getDuration();
                    kit_amplifier = effect.getAmplifier();
                }

                isBloodied = true;
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, amplifier)); // Strength I
                player.sendMessage("You feel the strength of bloodied rage!");
            }
        } else {
            if (isBloodied) {
                isBloodied = false;
                PotionEffectType potion = PotionEffectType.INCREASE_DAMAGE;
//                player.sendMessage(kit_amplifier + " " + duration);
                if (player.getPotionEffect(potion).getAmplifier() == amplifier && player.getPotionEffect(potion).getDuration() > 500) {
                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, duration, kit_amplifier));
                    duration = 0;
                }

                player.sendMessage("Your strength fades as you heal.");
            }
        }
    }

    // Static method to handle external disable calls
    public static void handlePlayerDisable(Player player) {
        Bloodied perk = activePerks.get(player.getUniqueId());
        if (perk != null) {
            perk.onDisable();
        }
    }

    public static boolean isActive(Player player) {
        return activePerks.containsKey(player.getUniqueId());
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

        // Doesnt make sense, since they can gain regen and effect healing
//        if (event.getAction() == EntityPotionEffectEvent.Action.REMOVED
//                && event.getModifiedType() == PotionEffectType.INCREASE_DAMAGE) {
            if (isActive(player)) {
                activePerks.get(player.getUniqueId()).checkHealthAndApplyEffect(player);
            }
//        }
    }
}
