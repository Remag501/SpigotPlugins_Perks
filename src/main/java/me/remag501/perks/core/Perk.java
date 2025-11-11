package me.remag501.perks.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Perk implements Cloneable, Listener {
    // Stores info about the perk
    protected UUID player; // may be removed in future
    private static final Map<PerkType,Map<UUID, Perk>> activePerks = new ConcurrentHashMap<>();
//    private final ItemStack perkItem;
    private PerkType perkType;
    private int quantity;
    private final boolean starPerk;
    private int stars;
    private final List<List<PerkType>> requirements; // On clone will shallow copy to save memory

    public void activatePlayer() {
        PerkType perkType = PerkType.getPerkType(this);
        Map<UUID, Perk> perks = activePerks.get(perkType);
        if (perks == null) {
            perks = new ConcurrentHashMap<>();
            activePerks.put(perkType, perks);
        }
        perks.put(player, this);
    }

    public void deactivatePlayer() {
        PerkType perkType = PerkType.getPerkType(this);
        Map<UUID, Perk> perks = activePerks.get(perkType);
        if (perks == null) {
            perks = new ConcurrentHashMap<>();
            activePerks.put(perkType, perks);
        }
        perks.remove(player, this);
    }

    public Perk getPerk(UUID uuid) {
        PerkType perkType = PerkType.getPerkType(this);
        Map<UUID, Perk> perks = activePerks.get(perkType);
        if (perks == null) {
            perks = new ConcurrentHashMap<>();
            activePerks.put(perkType, perks);
        }
        return perks.get(uuid);
    }

    public static Perk getPerk(UUID uuid, PerkType type) {
        // 1. Look up the Map<UUID, Perk> for the given PerkType.
        Map<UUID, Perk> perks = activePerks.get(type);
        if (perks == null) {
            // No player has this PerkType equipped.
            return null;
        }
        // 2. Look up the specific Perk instance for the player's UUID.
        return perks.get(uuid);
    }


    public Perk(boolean starPerk, List<List<PerkType>> requirements) {
        player = null;
        quantity = 1;
        this.starPerk = starPerk;
        stars = 1;
        this.requirements = requirements;
    }

    public Perk(boolean starPerk) {
        player = null;
        quantity = 1;
        this.starPerk = starPerk;
        stars = 1;
        this.requirements = null;
    }

    public Perk(List<List<PerkType>> requirements) {
        player = null;
        quantity = 1;
        this.starPerk = false;
        this.requirements = requirements;
    }

    public Perk() {
        player = null;
        quantity = 1;
        this.starPerk = false;
        this.requirements = null;
    }

    public List<List<PerkType>> getRequirements() {
        return requirements;
    }

    public boolean increaseStar() {
        if (stars < quantity)
            stars++;
        else
            return false;
        return true;
    }

    public boolean decreaseStar() {
        if (stars > 0)
            stars--;
        else
            return false;
        return true;
    }

    public void setStar(int stars) {
        this.stars = stars;
    }

    public int getStars() {
        return stars;
    }

    public void setPlayer(Player player) {
        this.player = player.getUniqueId();
    }

    public boolean addCount() {
        if (quantity >= 3) return false;
        quantity++;
        return true;
    }

    public boolean lowerCount() {
        if (quantity <= 0) return false;
        quantity--;
        return true;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isStarPerk() {
        return starPerk;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Perk)) return false;
//        return ((Perk) obj).i == this.perkItem;
        return true;
    }

    @Override
    public Perk clone(){
        try {
            return (Perk) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void onEnable();

    public abstract void onDisable();

}
