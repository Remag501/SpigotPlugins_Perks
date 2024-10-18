package me.remag501.perks.perkTypes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class Kangaroo extends Perk implements Listener {

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_TIME = 30 * 1000; // 30 seconds in milliseconds

    public Kangaroo(ItemStack perkItem) {
        super(perkItem);
    }

    @Override
    public void onEnable(Player player) {
//        player.sendMessage("Kangaroo Perk activated!");
        // Register event listeners for the player's server
        player.getServer().getPluginManager().registerEvents(this, player.getServer().getPluginManager().getPlugin("Perks"));
    }

    @Override
    public void onDisable(Player player) {
//        player.sendMessage("Kangaroo Perk deactivated!");
        // Unregister listeners when disabling the perk
        PlayerToggleFlightEvent.getHandlerList().unregister(this);
        PlayerMoveEvent.getHandlerList().unregister(this);
    }

    // Allow double jump only when the player is airborne and reset flight status on the ground
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.isOnGround()) {
            player.setAllowFlight(true); // Allow flight while on the ground
        }
    }

    // Handle double jump and apply cooldown
    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();

        // Check if player is in air and hasn't hit cooldown
        if (!player.isOnGround() && !hasCooldown(player)) {
            // Disable the player's flight and apply upward velocity for double jump
            player.setFlying(false);
            player.setAllowFlight(false);
            Vector jumpVelocity = player.getVelocity();
            jumpVelocity.normalize(); // Normalize the jump velocity to a unit vector
            jumpVelocity.multiply(1.5); // Increase the jump speed
            jumpVelocity.setY(1.0); //Adjust the upward velocity for double jump
            player.setVelocity(jumpVelocity);
            player.sendMessage("You used your double jump!");

            // Start cooldown for 30 seconds
            startCooldown(player);
        } else if (hasCooldown(player)) {
            player.sendMessage("Double jump is on cooldown! Wait a bit longer.");
        }

        event.setCancelled(true); // Prevent normal flight toggling
    }

    // Check if the player is still on cooldown
    private boolean hasCooldown(Player player) {
        if (cooldowns.containsKey(player.getUniqueId())) {
            long timeLeft = System.currentTimeMillis() - cooldowns.get(player.getUniqueId());
            return timeLeft < COOLDOWN_TIME;
        }
        return false;
    }

    // Start the cooldown for the player
    private void startCooldown(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage("Double jump is ready to use again!");
            }
        }.runTaskLater(player.getServer().getPluginManager().getPlugin("Perks"), 600L); // 600 ticks = 30 seconds
    }
}