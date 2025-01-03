package me.remag501.perks.perkTypes;

import org.bukkit.entity.Player;

public class PlayerPerkInstance {
    private final PerkType perkType;
    private final Player player;

    public PlayerPerkInstance(PerkType perkType, Player player) {
        this.perkType = perkType;
        this.player = player;
    }

    public PerkType getPerkType() {
        return perkType;
    }

    public Player getPlayer() {
        return player;
    }

    public void onEnable() {
        perkType.getPerk().onEnable();
    }

    public void onDisable() {
        perkType.getPerk().onDisable();
    }
}
