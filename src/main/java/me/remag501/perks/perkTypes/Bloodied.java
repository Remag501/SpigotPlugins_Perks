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

public class Bloodied extends Perk implements Listener {

    private static final double HEALTH_THRESHOLD = 0.25; // 25% health
    private static final Map<Player, Bloodied> activePerks = new HashMap<>();

    private BukkitTask healthCheckTask;
    private boolean isBloodied;
    private int duration;

    public Bloodied(ItemStack perkItem) {
        super(perkItem);
    }

    @Override
    public void onEnable() {
        activePerks.put(player, this);

        // Schedule a repeating task to check the player's health periodically
        healthCheckTask = Bukkit.getScheduler().runTaskTimer(
                player.getServer().getPluginManager().getPlugin("Perks"),
                () -> checkHealthAndApplyEffect(player),
                0L, 200L // Runs every 10 seconds
        );

        isBloodied = false;
        duration = 0;

//        Bukkit.getPluginManager().registerEvents(this, player.getServer().getPluginManager().getPlugin("Perks"));
    }

    @Override
    public void onDisable() {
        activePerks.remove(player);

        if (healthCheckTask != null) {
            healthCheckTask.cancel();
        }

        isBloodied = false;
        PotionEffectType potion = PotionEffectType.INCREASE_DAMAGE;

        if (player.isOnline() && player.hasPotionEffect(potion)
                && player.getPotionEffect(potion).getAmplifier() == 0
                && player.getPotionEffect(potion).getDuration() > 500) {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }

//        EntityDamageEvent.getHandlerList().unregister(this);
//        EntityRegainHealthEvent.getHandlerList().unregister(this);
//        EntityPotionEffectEvent.getHandlerList().unregister(this);
    }

    private void checkHealthAndApplyEffect(Player player) {
        double currentHealth = player.getHealth();
        double maxHealth = player.getMaxHealth();

        if (currentHealth > 0 && currentHealth / maxHealth <= HEALTH_THRESHOLD) {
            if (!isBloodied) {
                PotionEffect effect = player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE);
                duration = 0;

                if (effect != null) {
                    if (effect.getAmplifier() > 0)
                        return; // Don't apply bloodied effects if the user already has strength
                    duration = effect.getDuration();
                }

                isBloodied = true;
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0)); // Strength I
                player.sendMessage("You feel the strength of bloodied rage!");
            }
        } else {
            if (isBloodied) {
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

    // Static method to handle external disable calls
    public static void handlePlayerDisable(Player player) {
        Bloodied perk = activePerks.get(player);
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
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (isActive(player)) {
            activePerks.get(player).checkHealthAndApplyEffect(player);
        }
    }

    @EventHandler
    public void onPlayerHeal(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (isActive(player)) {
            activePerks.get(player).checkHealthAndApplyEffect(player);
        }
    }

    @EventHandler
    public void onPlayerLoseEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();
        // Doesnt make sense, since they can gain regen and effect healing
//        if (event.getAction() == EntityPotionEffectEvent.Action.REMOVED
//                && event.getModifiedType() == PotionEffectType.INCREASE_DAMAGE) {
            if (isActive(player)) {
                activePerks.get(player).checkHealthAndApplyEffect(player);
            }
//        }
    }
}
