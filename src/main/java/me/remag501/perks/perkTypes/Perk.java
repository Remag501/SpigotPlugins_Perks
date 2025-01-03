package me.remag501.perks.perkTypes;

import me.remag501.perks.utils.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class Perk implements Cloneable{
    // Stores info about the perk
    private ItemStack perkItem;
    protected Player player;
    private int quantity;

    public Perk(ItemStack perkItem) {
        this.perkItem = perkItem;
        player = null;
        quantity = 1;
    }

    public Perk(PerkType perkType, Player player) {
        this.perkItem = perkType.getItem();
        this.player = player;
        quantity = 1;
    }

    public void setPlayer(Player player) {
        this.player = player;
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

    public ItemStack getItem() {
        return perkItem;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Perk)) return false;
        return ((Perk) obj).perkItem == this.perkItem;
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
