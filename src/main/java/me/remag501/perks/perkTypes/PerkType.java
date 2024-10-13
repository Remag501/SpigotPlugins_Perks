package me.remag501.perks.perkTypes;

import me.remag501.perks.utils.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

//new Perk("Sword Perk", Items.createItem(Material.DIAMOND_SWORD, "Sword Perk", false, "stuff")
public enum PerkType {
    SWORD_PERK(new LongSwordPerk(Items.createPerkItem(Material.DIAMOND_SWORD, "Sword Perk", "SWORD_PERK", 1, "stuff"))),
    CREEPER_BRAWLER(new CreeperBrawler(Items.createPerkItem(Material.CREEPER_HEAD, "Creeper Brawler Perk", "CREEPER_BRAWLER", 2, "Creeper Brawler Perk"))),
    HOT_HANDS(new HotHandsPerk(Items.createPerkItem(Material.BAKED_POTATO, "Hot Hands", "HOT_HANDS", 3, "Hot potato got a little too hot"))),
    GOLDEN_EXPERIENCE(new GoldenExperiencePerk(Items.createPerkSkull("http://textures.minecraft.net/texture/a5e048e5e94b945d161f0f3df83cc6f61985d5974c7ad9595197a50dc00edc0b",
            "Golden Experience", "GOLDEN_EXPERIENCE", 4, "Muda Muda Muda")));
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
