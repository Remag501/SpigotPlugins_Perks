package me.remag501.perks.perktypes;

import me.remag501.perks.core.Perk;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PackMaster Perk:
 * Effect: A wolf is summoned upon killing a player.
 * Requirements: Must track summoned wolves and despawn them on perk unequip.
 */
public class PackMaster extends Perk {

    // Unique state for THIS cloned instance: List of wolves summoned by this perk
    private final List<UUID> summonedWolves;

    public PackMaster(ItemStack perkItem) {
        super(perkItem);
        summonedWolves = new ArrayList<>();
    }

    // --- Per-Player Lifecycle Hooks ---

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        // Despawn all owned wolves when the perk is deactivated
        this.despawnAllWolves();
    }

    // --- Event Handling (On the PROTOTYPE Listener) ---

    @EventHandler(ignoreCancelled = true)
    public void onPlayerKill(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null) return; // Not killed by a player

        UUID killerUuid = killer.getUniqueId();

        // 1. Centralized Lookup: Get the active, cloned instance for the killer
        PackMaster perk = (PackMaster) getPerk(killerUuid);
        if (perk == null) return; // Killer doesn't have the perk equipped

        // 2. Summon the wolf
        perk.summonWolf(killer);

        // Optional: Add visual feedback (sound/message)
        killer.sendMessage("§6A new wolf joins your pack!");
    }

    /**
     * Spawns a tamed wolf for the player and registers it to this perk instance.
     */
    private void summonWolf(Player owner) {
        // Spawn the wolf at the owner's location
        Location location = owner.getLocation();
        Wolf wolf = (Wolf) location.getWorld().spawnEntity(location, EntityType.WOLF);

        // Tame the wolf and set the owner
        wolf.setTamed(true);
        wolf.setOwner(owner);
        wolf.setCollarColor(org.bukkit.DyeColor.BROWN); // Optional: differentiate perk wolves

        // Add the wolf's UUID to the instance state for tracking/cleanup
        this.summonedWolves.add(wolf.getUniqueId());
    }

    /**
     * Despawns all wolves tracked by this specific perk instance.
     */
    private void despawnAllWolves() {
        if (this.summonedWolves.isEmpty()) return;

        // Iterate through all UUIDs and attempt to find and remove the entity
        for (UUID wolfUuid : this.summonedWolves) {
            Entity entity = Bukkit.getEntity(wolfUuid);
            if (entity != null && entity.isValid() && entity.getType() == EntityType.WOLF) {
                entity.remove();
            }
        }

        // Clear the list after removal
        this.summonedWolves.clear();
    }
}