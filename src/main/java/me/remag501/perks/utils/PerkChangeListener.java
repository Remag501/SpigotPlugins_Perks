package me.remag501.perks.utils;

import me.remag501.perks.perkTypes.Perk;
import me.remag501.perks.perkTypes.PlayerPerks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PerkChangeListener implements Listener {

    public static List<String> enabledWorlds = new ArrayList<String>();

    public PerkChangeListener() {

    }

    public PerkChangeListener(List<String> disabledWorlds) {
        this.enabledWorlds = disabledWorlds;
    }

    private void checkAllowedWorld(Player player) {
        String newWorld = player.getWorld().getName();

        // Check does not allow the world allows perks
        if (!enabledWorlds.contains(newWorld)) {
            // Disable player's perks
            disablePlayerPerks(player);
        } else {
            // Re-enable player's perks
            enablePlayerPerks(player);
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        checkAllowedWorld(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Turn off perk
        disablePlayerPerks(event.getEntity());
        // Player loses on perk at random
        // If the player died at combat the killers gets the perk
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        checkAllowedWorld(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Check if player has perks loaded
        Player player = event.getPlayer();
        PlayerPerks playerPerks = PlayerPerks.getPlayerPerks(player.getUniqueId());
        if (playerPerks == null)
            new PlayerPerks(((Player) player).getUniqueId());
        // Check if player can enable their perks
        checkAllowedWorld(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        disablePlayerPerks(event.getPlayer());
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
