package me.remag501.perks.perkTypes;

import me.remag501.perks.utils.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Perk {
    // Stores info about the perk
    private Player player;
    private ItemStack perkItem;

    public Perk(Player player, ItemStack perkItem) {
        this.player = player;
        this.perkItem = perkItem;
    }

    public ItemStack getItem() {
        return perkItem;
    }

    public abstract void onEnable(Player player);

    public abstract void onDisable(Player player);

}
