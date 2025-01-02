package me.remag501.perks.perkTypes;

import me.remag501.perks.utils.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class Perk {
    // Stores info about the perk
    private ItemStack perkItem;
//    private List<Perk> perkDependencies;

    public Perk(ItemStack perkItem) {
        this.perkItem = perkItem;
    }

//    public Perk(ItemStack perkItem, List<Perk> perkDependencies) {
//        this.perkItem = perkItem;
//        this.perkDependencies = perkDependencies;
//    }

    public ItemStack getItem() {
        return perkItem;
    }

    public abstract void onEnable(Player player);

    public abstract void onDisable(Player player);

}
