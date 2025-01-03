package me.remag501.perks.perkTypes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class Jumper extends Perk {

    private BukkitTask slownessTask;

    public Jumper(ItemStack perkItem) {
        super(perkItem);
    }

    @Override
    public void onEnable() {
//        player.sendMessage("Jumper Perk activated!");

        // Apply Jump Boost I effect when the perk is enabled
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 0)); // Jump Boost I

        // Schedule a repeating task that applies Slowness every 1.5 minutes (1800 ticks)
        slownessTask = Bukkit.getScheduler().runTaskTimer(
                player.getServer().getPluginManager().getPlugin("Perks"),
                () -> applySlowness(player),
                1800L, 1800L // Runs every 1.5 minutes (1800 ticks = 90 seconds)
        );
    }

    @Override
    public void onDisable() {
//        player.sendMessage("Jumper Perk deactivated!");

        // Remove Jump Boost and cancel the slowness task when the perk is disabled
        player.removePotionEffect(PotionEffectType.JUMP);

        if (slownessTask != null) {
            slownessTask.cancel();
        }
    }

    // Method to apply slowness for 5 seconds (100 ticks)
    private void applySlowness(Player player) {
        if (player.isOnline()) {
            player.sendMessage("You feel tired from jumping!");
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0)); // Slowness I for 5 seconds
        }
    }
}
