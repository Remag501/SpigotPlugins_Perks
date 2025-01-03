package me.remag501.perks.perkTypes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class Jumper extends Perk {

    private static final Map<Player, Jumper> activePerks = new HashMap<>();
    private BukkitTask slownessTask;

    public Jumper(ItemStack perkItem) {
        super(perkItem);
    }

    @Override
    public void onEnable() {
        activePerks.put(player, this);
        // Apply Jump Boost I effect when the perk is enabled
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 0)); // Jump Boost I

        // Schedule a repeating task that applies Slowness every 1.5 minutes (1800 ticks)
        slownessTask = Bukkit.getScheduler().runTaskTimer(
                player.getServer().getPluginManager().getPlugin("Perks"),
                () -> applySlowness(player),
                1800L, 1800L // Runs every 1.5 minutes
        );
    }

    @Override
    public void onDisable() {
        activePerks.remove(player);

        // Remove Jump Boost and cancel the slowness task when the perk is disabled
        player.removePotionEffect(PotionEffectType.JUMP);

        if (slownessTask != null) {
            slownessTask.cancel();
        }
    }

    private void applySlowness(Player player) {
        if (player.isOnline()) {
            player.sendMessage("You feel tired from jumping!");
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0)); // Slowness I for 5 seconds
        }
    }

    // Static method to handle external disable calls
    public static void handlePlayerDisable(Player player) {
        Jumper perk = activePerks.get(player);
        if (perk != null) {
            perk.onDisable();
        }
    }

    public static boolean isActive(Player player) {
        return activePerks.containsKey(player);
    }
}
