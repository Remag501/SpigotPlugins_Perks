package me.remag501.perks.perkTypes;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

public class PlayerPerks {

    private static HashMap<UUID, PlayerPerks> playerPerks = new HashMap<>();
    private UUID playerUUID;
    private ArrayList<Perk> equippedPerks;
    private ArrayList<Perk> ownedPerks;

    public static PlayerPerks getPlayerPerks(UUID playerUUID) {
        return playerPerks.get(playerUUID);
    }

    public ArrayList<Perk> getEquippedPerks() {
        return equippedPerks;
    }

    public ArrayList<Perk> getOwnedPerks() {
        return ownedPerks;
    }

    public PlayerPerks(UUID playerUUID) {
        equippedPerks = new ArrayList<Perk>();
        ownedPerks = new ArrayList<Perk>();
        playerPerks.put(playerUUID, this);
        this.playerUUID = playerUUID;
    }

    public void removeEquippedPerk(PerkType perkType) {
        if (!equippedPerks.contains(perkType.getPerk()))
            return;
        // Disable the perk before removing it
        Perk perk = perkType.getPerk();
        equippedPerks.remove(perk);
        perk.onDisable(Bukkit.getPlayer(playerUUID));
    }

    public void addEquippedPerk(PerkType perkType) {
        if (equippedPerks.contains(perkType.getPerk()))
            return;
        // Enable the perk before adding it
        equippedPerks.add(perkType.getPerk());
        // Only enables if the player is not in spawn
        if (Bukkit.getPlayer(playerUUID).getWorld().getName().equalsIgnoreCase("world"))
            return;
        perkType.getPerk().onEnable(Bukkit.getPlayer(playerUUID));
    }

    public boolean addOwnedPerks(PerkType perkType) {
        // O(n) complexity
        int count = Collections.frequency(ownedPerks, perkType.getPerk());
        if (count < 3) {
            ownedPerks.add(perkType.getPerk());
            return true;
        }
        return false;
    }

    public void removeOwnedPerk(PerkType perkType) {
        // Doesnt need count checker since remove leaves array the same if the value is not found
        // O(1) complexity
//        Bukkit.getPluginManager().getPlugin("Perks").getLogger().info(getEquippedPerks().toString());
//        Bukkit.getPluginManager().getPlugin("Perks").getLogger().info(getOwnedPerks().toString());
        if (!ownedPerks.contains(perkType.getPerk()))
            return;
        ownedPerks.remove(perkType.getPerk());
    }
}
