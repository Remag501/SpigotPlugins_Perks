package me.remag501.perks.perkTypes;

import me.remag501.perks.core.Perk;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Flash extends Perk {

    // Static map to track active perks per player
    private static final Map<UUID, Flash> activePerks = new HashMap<>();

    private BukkitTask weaknessTask;

    public Flash(ItemStack perkItem) {
        super(perkItem);
    }

    @Override
    public void onEnable() {
        // Ensure only one instance per player is active
        Player player = Bukkit.getPlayer(this.player);
        activePerks.put(this.player, this);

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
    public void onDisable() {
        Player player = Bukkit.getPlayer(this.player);
        // Remove the player from the active perks map
        activePerks.remove(player.getUniqueId());

        // Remove Speed and cancel the weakness task when the perk is disabled
        player.removePotionEffect(PotionEffectType.SPEED);

        if (weaknessTask != null) {
            weaknessTask.cancel();
        }
    }

    // Static method for handling player-specific behavior
    public static void handlePlayerDisable(Player player) {
        Flash perk = activePerks.get(player);
        if (perk != null) {
            perk.onDisable();
        }
    }

    // Static method to check if a player has the perk active
    public static boolean isActive(Player player) {
        return activePerks.containsKey(player);
    }

    // Method to apply Weakness for 4 seconds (80 ticks)
    private void applyWeakness(Player player) {
        if (isActive(player)) { // Ensure the perk is still active
            player.sendMessage("You feel weak from running too fast!");
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 80, 0)); // Weakness I for 4 seconds
        }
    }
}
