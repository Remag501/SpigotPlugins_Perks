package me.remag501.perks.perkTypes;

import java.util.List;
import java.util.UUID;

public class PlayerPerks {

    private UUID playterUUID;
    private List<Perk> ownedPerks;

    public List<Perk> getOwnedPerks() {
        return ownedPerks;
    }

    public PlayerPerks() {

    }

    public PlayerPerks(UUID playterUUID, List<Perk> perks) {

    }
}
