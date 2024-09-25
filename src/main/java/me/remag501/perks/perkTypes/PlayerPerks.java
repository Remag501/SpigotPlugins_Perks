package me.remag501.perks.perkTypes;

import org.bukkit.Bukkit;

import java.util.ArrayList;
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

    public void removePerk(PerkType perkType) {
        if (!equippedPerks.contains(perkType.getPerk()))
            return;
        // Disable the perk before removing it
        Perk perk = perkType.getPerk();
        equippedPerks.remove(perk);
        perk.onDisable(Bukkit.getPlayer(playerUUID));
    }

    public void addPerk(PerkType perkType) {
        if (equippedPerks.contains(perkType.getPerk()))
            return;
        // Enable the perk before adding it
        equippedPerks.add(perkType.getPerk());
        perkType.getPerk().onEnable(Bukkit.getPlayer(playerUUID));
    }
}
