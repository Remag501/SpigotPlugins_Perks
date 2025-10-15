package me.remag501.perks.perktypes;

import me.remag501.perks.core.Perk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class DamageMultiplierTwoPerk extends Perk {

    public DamageMultiplierTwoPerk(ItemStack perkItem) {
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
            // You can check if the player has the perk enabled, then modify damage
            // Assuming you have a method to check if the player has the perk
            double damage = event.getDamage();
            event.setDamage(damage * 2); // Multiply damage by 1.5
        }
    }
}
