package me.remag501.perks.perkTypes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class LowMaintenance extends Perk implements Listener {

    private BukkitTask saturationTask;

    public LowMaintenance(ItemStack perkItem) {
        super(perkItem);
    }

    @Override
    public void onEnable() {
//        player.sendMessage("Low Maintenance Perk activated!");

        // Start a repeating task that applies Saturation I every 2 minutes (2400 ticks)
        saturationTask = new BukkitRunnable() {
            @Override
            public void run() {
                applySaturation(player);
            }
        }.runTaskTimer(player.getServer().getPluginManager().getPlugin("Perks"), 0L, 2400L); // 2400 ticks = 2 minutes
    }

    @Override
    public void onDisable() {
//        player.sendMessage("Low Maintenance Perk deactivated!");

        // Cancel the saturation task when the perk is disabled
        if (saturationTask != null) {
            saturationTask.cancel();
        }
    }

    // Method to apply Saturation I for 15 seconds (300 ticks)
    private void applySaturation(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 300, 0)); // Saturation I for 15 seconds
        player.sendMessage("You feel well-fed with Low Maintenance.");
    }
}
