package me.remag501.perks.perkTypes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class GoldenExperiencePerk extends Perk implements Listener {

    public GoldenExperiencePerk(ItemStack item) {
        super(item);
    }

    @Override
    public void onEnable(Player player) {
        player.sendMessage("Golden Experience perk activated! Summon frogs to fight for you.");
        // Register event listeners
        Bukkit.getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugin("Perks"));
    }

    @Override
    public void onDisable(Player player) {
        player.sendMessage("Golden Experience perk deactivated.");
        // Unregister event listeners
        BlockBreakEvent.getHandlerList().unregister(this);
        EntityTargetLivingEntityEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        // Check if the player has the Golden Experience perk enabled
        if (hasPerkEnabled(player)) {
            // Prevent the block from dropping items
            event.setDropItems(false);
            // Summon a frog at the block's location
            Location loc = event.getBlock().getLocation().add(0.5, 1, 0.5);
            Wolf frog = (Wolf) player.getWorld().spawnEntity(loc, EntityType.WOLF);

            player.sendMessage("A frog has been summoned to fight for you!");

            // Set frog AI to attack nearby hostile mobs and players
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!frog.isValid()) {
                        cancel();
                        return;
                    }

                    // Find the nearest hostile mob or player within a radius (e.g., 10 blocks)
                    List<LivingEntity> nearbyEntities = frog.getWorld().getNearbyEntities(frog.getLocation(), 10, 10, 10)
                            .stream()
                            .filter(entity -> entity instanceof LivingEntity)
                            .map(entity -> (LivingEntity) entity)
                            .collect(Collectors.toList());

                    LivingEntity nearestTarget = nearbyEntities.stream()
                            .filter(entity -> entity instanceof Monster || entity instanceof Player && !entity.equals(player))
                            .findFirst()
                            .orElse(null);

                    // If there's a hostile mob or player nearby, set the frog to target it
                    if (nearestTarget != null) {
                        frog.setTarget(nearestTarget);
                    }
                }
            }.runTaskTimer(Bukkit.getPluginManager().getPlugin("Perks"), 0L, 20L); // Check every second
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        // Prevent frogs from attacking the player who summoned them
        if (event.getEntity() instanceof Frog && event.getTarget() instanceof Player) {
            Player target = (Player) event.getTarget();
            Frog frog = (Frog) event.getEntity();

            if (target.equals(frog.getCustomName())) {
                event.setCancelled(true);
            }
        }
    }

    private boolean hasPerkEnabled(Player player) {
        // Your logic to check if the player has this perk enabled
        return true; // For now, assume perk is enabled
    }
}
