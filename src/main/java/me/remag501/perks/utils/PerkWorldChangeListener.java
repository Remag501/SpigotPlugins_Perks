package me.remag501.perks.utils;

import me.remag501.perks.perkTypes.Perk;
import me.remag501.perks.perkTypes.PlayerPerks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PerkWorldChangeListener implements Listener {

    private List<String> disabledWorlds;

    public PerkWorldChangeListener() {
        disabledWorlds = new ArrayList<String>();
        disabledWorlds.add("world");
    }

    public PerkWorldChangeListener(List<String> disabledWorlds) {
        this.disabledWorlds = disabledWorlds;
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("event triggered");
        String newWorld = player.getWorld().getName();

        // Check if the world allows perks
        if (disabledWorlds.contains(newWorld)) {
            // Disable player's perks
            disablePlayerPerks(player);
        } else {
            // Re-enable player's perks
            enablePlayerPerks(player);
        }
    }

    private void disablePlayerPerks(Player player) {
        player.sendMessage("Perks are disabled in this world.");
        // Loop through the player's active perks and disable them
        for (Perk perk : getPlayerActivePerks(player)) {
            perk.onDisable(player);
        }
    }

    private void enablePlayerPerks(Player player) {
        player.sendMessage("Perks are enabled in this world.");
        // Loop through the player's active perks and enable them
        for (Perk perk : getPlayerActivePerks(player)) {
            perk.onEnable(player);
        }
    }

    private List<Perk> getPlayerActivePerks(Player player) {
        return PlayerPerks.getPlayerPerks(player.getUniqueId()).getEquippedPerks();
    }
}
