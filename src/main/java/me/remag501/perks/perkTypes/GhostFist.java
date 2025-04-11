package me.remag501.perks.perkTypes;

import me.remag501.perks.core.Perk;
import me.remag501.perks.core.PerkType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.*;

public class GhostFist extends Perk implements Listener {

    private static final Map<UUID, GhostFist> activePerks = new HashMap<>();

    public GhostFist(ItemStack perkItem, List<List<PerkType>> requirements) {
        super(perkItem, requirements);
    }

    @Override
    public void onEnable() {
        activePerks.put(this.player, this);
    }

    @Override
    public void onDisable() {
        activePerks.remove(this.player);
    }

    // Add this constant to avoid hardcoding your plugin name
    private static final String GHOST_FIST_META = "ghost_fist_hit";

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager)) return;
        if (!(event.getEntity() instanceof LivingEntity entity)) return;
        if (entity instanceof ArmorStand) return;

        // 🧼 Prevent recursion: ignore if this is a ghost fist echo
        if (entity.hasMetadata(GHOST_FIST_META))
            return;


        GhostFist perk = activePerks.get(damager.getUniqueId());
        if (perk == null) return;

        // ✅ Ensure the player is using an empty hand
        ItemStack itemInHand = damager.getInventory().getItemInMainHand();
        if (itemInHand.getType() != Material.AIR) return;

        double damage = event.getDamage();

        // 🧠 Save knockback direction (for realism/randomization)
        Vector knockback = damager.getLocation().getDirection().normalize().multiply(0.4 + Math.random() * 0.3);
        knockback.setY(0.35 + Math.random() * 0.2);

        // ⏳ Schedule ghost attack
        Bukkit.getScheduler().runTaskLater(
                Bukkit.getPluginManager().getPlugin("Perks"), // Swap to your plugin instance
                () -> {
                    if (!entity.isDead()) {
                        try {
                            // 🏷 Mark the entity to skip re-processing
                            entity.setMetadata(GHOST_FIST_META, new FixedMetadataValue(
                                    Bukkit.getPluginManager().getPlugin("Perks"), true));

                            entity.damage(damage, damager);
//                            entity.setVelocity(knockback);

                            damager.sendMessage("§7👊 Ghost Fist echoes through the air!");
                        } finally {
                            // 🧼 Remove tag immediately after
                            entity.removeMetadata(GHOST_FIST_META,
                                    Bukkit.getPluginManager().getPlugin("Perks"));
                        }
                    }
                },
                11L // Just enough ticks before invulnerability expires
        );
    }
}
