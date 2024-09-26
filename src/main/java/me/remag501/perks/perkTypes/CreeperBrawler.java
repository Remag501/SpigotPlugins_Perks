package me.remag501.perks.perkTypes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class CreeperBrawler extends Perk implements Listener {

    public CreeperBrawler(ItemStack perkItem) {
        super(perkItem);
    }

    @Override
    public void onEnable(Player player) {
        player.sendMessage("Creeper Damage Perk activated! You now deal 1.5x damage to creepers.");
        // Register the event listener when the perk is enabled
        player.getServer().getPluginManager().registerEvents(this, player.getServer().getPluginManager().getPlugin("Perks"));
    }

    @Override
    public void onDisable(Player player) {
        player.sendMessage("Creeper Damage Perk disabled! Your damage to creepers is now normal.");
        // When the perk is disabled, you might want to unregister the listener.
        // You can store listener references elsewhere for more control.
    }

    // Listen to the EntityDamageByEntityEvent to detect when the player damages a creeper
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getDamager();
        Entity entity = event.getEntity();

        // Check if the damaged entity is a creeper
        if (entity.getType() == EntityType.CREEPER) {
            // Apply 1.5x damage multiplier to creepers
            double newDamage = event.getDamage() * 15;
            event.setDamage(newDamage);
            player.sendMessage("You dealt extra damage to the creeper!");
        }
    }
}
