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

public class Resistant extends Perk implements Listener {

    private double healthThreshold; // Varies by stars acquired
    private int amplifier;
    private static final Map<UUID, Resistant> activePerks = new HashMap<>();

    private BukkitTask healthCheckTask;
    private boolean isBloodied;
    private int duration;
    private int kit_amplifier;

    public Resistant(ItemStack perkItem, boolean starPerk) {
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
                healthThreshold = 0.20;
                amplifier = 0;
                break;
            case 2:
                healthThreshold = 0.25;
                amplifier = 0;
                break;
            case 3:
                healthThreshold = 0.3;
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
        PotionEffectType potion = PotionEffectType.DAMAGE_RESISTANCE;

        if (player.isOnline() && player.hasPotionEffect(potion)
                && player.getPotionEffect(potion).getAmplifier() == amplifier
                && player.getPotionEffect(potion).getDuration() > 500) {
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        }

    }

    private void checkHealthAndApplyEffect(Player player) {
        double currentHealth = player.getHealth();
        double maxHealth = player.getMaxHealth();

        if (currentHealth > 0 && currentHealth / maxHealth <= healthThreshold) {
            if (!isBloodied) {
                PotionEffect effect = player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                duration = 0;

                if (effect != null) {
                    if (effect.getAmplifier() > amplifier)
                        return; // Don't apply bloodied effects if the user already has strength
                    duration = effect.getDuration();
                    kit_amplifier = effect.getAmplifier();
                }

                isBloodied = true;
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, amplifier)); // Strength I
                player.sendMessage("You are resistant due to being low hp!");
            }
        } else {
            if (isBloodied) {
                isBloodied = false;
                PotionEffectType potion = PotionEffectType.DAMAGE_RESISTANCE;
//                player.sendMessage(kit_amplifier + " " + duration);
                if (player.getPotionEffect(potion).getAmplifier() == amplifier && player.getPotionEffect(potion).getDuration() > 500) {
                    player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, kit_amplifier));
                    duration = 0;
                }

                player.sendMessage("Your resistance fades as you heal.");
            }
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
        if (isActive(player)) {
            activePerks.get(player.getUniqueId()).checkHealthAndApplyEffect(player);
        }
    }
}
