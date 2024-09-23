package me.remag501.perks.perkTypes;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LongSwordPerk extends Perk {

    public LongSwordPerk(ItemStack perkItem) {
        super(perkItem);
    }

    @Override
    public void onEnable(Player player) {
        player.sendMessage("Your perk has been activated!");
    }

    @Override
    public void onDisable(Player player) {
        player.sendMessage("Your perk has been disabled!");
    }
}
