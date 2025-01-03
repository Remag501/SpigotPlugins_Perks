package me.remag501.perks.perkTypes;

import org.bukkit.entity.Creeper;
import org.bukkit.event.Listener;
import org.bukkit.event.HandlerList;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CreeperBrawler extends Perk implements Listener {

    public CreeperBrawler(ItemStack perkItem) {
        super(perkItem);
    }

    @Override
    public void onEnable() {
        player.sendMessage("Creeper Damage Perk activated!");
        // You may want to register the event listener here
        player.getServer().getPluginManager().registerEvents(this, player.getServer().getPluginManager().getPlugin("Perks"));
    }

    @Override
    public void onDisable() {
        player.sendMessage("Creeper Damage Perk deactivated!");
        // Deregister the event listener when the perk is disabled
        HandlerList.unregisterAll(this);
    }

    // Example event handler for the perk
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Check if the attacker is the player with the perk
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Creeper) {
            Player player = (Player) event.getDamager();
            // Apply 1.5x damage if the player has the perk enabled
            event.setDamage(event.getDamage() * 15);
        }
    }
}