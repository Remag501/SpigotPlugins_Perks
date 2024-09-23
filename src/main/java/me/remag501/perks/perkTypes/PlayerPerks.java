package me.remag501.perks.perkTypes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerPerks {

    private static HashMap<UUID, PlayerPerks> playerPerks = new HashMap<>();
    private UUID playerUUID;
    private ArrayList<Perk> ownedPerks;

    public static PlayerPerks getPlayerPerks(UUID playerUUID) {
        return playerPerks.get(playerUUID);
    }

    public ArrayList<Perk> getOwnedPerks() {
        return ownedPerks;
    }

    public PlayerPerks(UUID playerUUID) {
        ownedPerks = new ArrayList<Perk>();
        playerPerks.put(playerUUID, this);
        this.playerUUID = playerUUID;
    }

    public void removePerk(PerkType perkType) {
        if (!ownedPerks.contains(perkType.getPerk()))
            return;
        // Disable the perk before removing it
        Perk perk = perkType.getPerk();
        ownedPerks.remove(perk);
        perk.onDisable(Bukkit.getPlayer(playerUUID));
    }

    public void addPerk(PerkType perkType) {
        if (ownedPerks.contains(perkType.getPerk()))
            return;
        // Enable the perk before adding it
        ownedPerks.add(perkType.getPerk());
        perkType.getPerk().onEnable(Bukkit.getPlayer(playerUUID));
    }
}
