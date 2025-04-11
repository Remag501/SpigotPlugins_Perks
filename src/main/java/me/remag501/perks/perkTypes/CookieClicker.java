package me.remag501.perks.perkTypes;

import me.remag501.perks.core.Perk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CookieClicker extends Perk implements Listener {
    private static final Map<UUID, CookieClicker> perkInstances = new HashMap<>();

    public CookieClicker(ItemStack perkItem) {
        super(perkItem);
    }

    @Override
    public void onEnable() {
        if (player != null) {
            perkInstances.put(player, this);
        }
    }

    @Override
    public void onDisable() {
        perkInstances.remove(player);
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null) return; // No killer, ignore

        UUID uuid = killer.getUniqueId();
        CookieClicker perk = perkInstances.get(uuid);
        if (perk == null) return; // Player doesn't have the perk equipped

        // Drop two cookies at the victim's location
        World world = event.getEntity().getWorld();
        world.dropItemNaturally(event.getEntity().getLocation(), new ItemStack(Material.COOKIE, 2));
    }
}
