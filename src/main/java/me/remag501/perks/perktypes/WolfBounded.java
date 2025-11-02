package me.remag501.perks.perktypes;

import me.remag501.perks.core.Perk;
import me.remag501.perks.core.PerkType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class WolfBounded extends Perk {

    PackMaster packMasterInstance;

    public WolfBounded(ItemStack perkItem) {
        super(perkItem);
    }

    @Override
    public void onEnable() {
        packMasterInstance = (PackMaster) getPerk(this.player, PerkType.PACK_MASTER);
    }

    @Override
    public void onDisable() {
        packMasterInstance = null;
    }

    @EventHandler
    public void onWolfHit(EntityDamageEvent event) {
        Entity damagedEntity = event.getEntity();

        // 1. Check if the damaged entity is a Wolf
        if (!(damagedEntity instanceof Wolf)) return;

        // 2. Optimization: Loop through ALL active WolfBounded instances (prototypes)
        // to see if the damaged wolf belongs to any of them.
        // The most direct way is to check the current instance's PackMaster.

        // Check if *this* cloned instance has a valid PackMaster reference (meaning it's equipped)
        if (this.packMasterInstance == null) return;

        UUID wolfId = damagedEntity.getUniqueId();

        // 3. Use the getter from PackMaster to see if this wolf belongs to the player.
        if (this.packMasterInstance.getSummonedWolves().contains(wolfId)) {
            // Get the player associated with this perk instance
            Player owner = Bukkit.getPlayer(this.player);

            if (owner != null) {
                owner.sendMessage("§cYour wolf has been hit! Health remaining: §e" +
                        String.format("%.1f", ((Wolf)damagedEntity).getHealth() - event.getFinalDamage()));
            }
            // Optional: Consume the event so only one perk triggers the message.
        }
    }

}
