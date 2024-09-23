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

    public static PlayerPerks getPlayerPerks(UUID playerUUID) {
        return playerPerks.get(playerUUID);
    }

    private ArrayList<Perk> ownedPerks;

    public ArrayList<Perk> getOwnedPerks() {
        return ownedPerks;
    }

    public PlayerPerks(UUID playerUUID) {
        ownedPerks = new ArrayList<Perk>();
        playerPerks.put(playerUUID, this);
        this.playerUUID = playerUUID;
    }

    public void addPerk(Perk perk) {
        ownedPerks.add(perk);
        perk.onEnable(Bukkit.getPlayer(playerUUID));
    }

//    public void addPerk(PerkType perk) {
//        ownedPerks.add(perk.getPerk());
//    }
}
