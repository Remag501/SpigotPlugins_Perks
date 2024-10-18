package me.remag501.perks.perkTypes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class Flash extends Perk {

    private BukkitTask weaknessTask;

    public Flash(ItemStack perkItem) {
        super(perkItem);
    }

    @Override
    public void onEnable(Player player) {
//        player.sendMessage("Flash Perk activated!");

        // Apply Speed I effect when the perk is enabled
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0)); // Speed I

        // Schedule a repeating task that applies Weakness every 2 minutes (2400 ticks)
        weaknessTask = Bukkit.getScheduler().runTaskTimer(
                player.getServer().getPluginManager().getPlugin("Perks"),
                () -> applyWeakness(player),
                2400L, 2400L // Runs every 2 minutes (2400 ticks = 120 seconds)
        );
    }

    @Override
    public void onDisable(Player player) {
//        player.sendMessage("Flash Perk deactivated!");

        // Remove Speed and cancel the weakness task when the perk is disabled
        player.removePotionEffect(PotionEffectType.SPEED);

        if (weaknessTask != null) {
            weaknessTask.cancel();
        }
    }

    // Method to apply Weakness for 4 seconds (80 ticks)
    private void applyWeakness(Player player) {
        if (player.isOnline()) {
            player.sendMessage("You feel weak from running too fast!");
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 80, 0)); // Weakness I for 4 seconds
        }
    }
}
