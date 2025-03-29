package me.remag501.perks.perkTypes;

import me.remag501.perks.core.Perk;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class FlatDamagePerk extends Perk implements Listener {
    private final double extraDamage = 2.0; // Additional flat damage

    public FlatDamagePerk(ItemStack perkItem) {
        super(perkItem);
    }

    @Override
    public void onEnable() {
        // Register the event listener when the perk is enabled
//        player.getServer().getPluginManager().registerEvents(this, player.getServer().getPluginManager().getPlugin("Perks"));
    }

    @Override
    public void onDisable() {
        // Unregister the event listener when the perk is disabled
//        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            // Check if the player has this perk enabled
            double finalDamage = event.getFinalDamage(); // Get the current final damage
            event.setDamage(finalDamage); // Don't modify the final damage here, keep it as is
            // Apply flat damage directly to the entity
            Entity entity = event.getEntity();
            EntityDamageEvent damageEvent = new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.CUSTOM, 2);
            Bukkit.getPluginManager().callEvent(damageEvent);

            if (!damageEvent.isCancelled()) {
                ((LivingEntity) entity).setHealth(Math.max(0, ((LivingEntity) entity).getHealth() - damageEvent.getFinalDamage()));
            }
        }
    }
}
