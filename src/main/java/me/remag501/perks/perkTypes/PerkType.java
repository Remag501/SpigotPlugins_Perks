package me.remag501.perks.perkTypes;

import me.remag501.perks.utils.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

//new Perk("Sword Perk", Items.createItem(Material.DIAMOND_SWORD, "Sword Perk", false, "stuff")
public enum PerkType {
    SWORD_PERK(new LongSwordPerk(Items.createItem(Material.DIAMOND_SWORD, "Sword Perk", "SWORD_PERK", false, "stuff"))),
    CREEPER_BRAWLER(new CreeperBrawler(Items.createItem(Material.CREEPER_HEAD, "Creeper Brawler Perk", "CREEPER_BRAWLER", false, "Creeper Brawler Perk"))),
    HOT_HANDS(new HotHandsPerk(Items.createItem(Material.BAKED_POTATO, "Hot Hands", "HOT_HANDS", false, "Hot potato got a little too hot")));
    private final Perk perk;
//    public final static int size = 1;

    PerkType(Perk perk) {
        this.perk = perk;
    }

    public Perk getPerk() {
        return perk;
    }

    public ItemStack getItem() {
        return perk.getItem();
    }

}
