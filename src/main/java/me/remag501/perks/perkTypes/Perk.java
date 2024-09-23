package me.remag501.perks.perkTypes;

import me.remag501.perks.utils.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Perk {
    // Stores info about the perk
    private String perkName;
    private ItemStack perkItem;

    public String getName() {
        return perkName;
    }

    public Perk(String perkName, ItemStack perkItem) {
        this.perkName = perkName;
        this.perkItem = perkItem;
    }

    public ItemStack getItem() {
        return perkItem;
    }

}
