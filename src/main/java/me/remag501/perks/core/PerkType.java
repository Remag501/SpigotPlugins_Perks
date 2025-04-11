package me.remag501.perks.core;

import me.remag501.perks.perkTypes.*;
import me.remag501.perks.utils.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

//new Perk("Sword Perk", Items.createItem(Material.DIAMOND_SWORD, "Sword Perk", false, "stuff")
public enum PerkType {
    SWORD_PERK(new LongSwordPerk(Items.createPerkItem(Material.DIAMOND_SWORD, "Sword Perk", "SWORD_PERK", 4, "stuff"))),
    CREEPER_BRAWLER(new CreeperBrawler(Items.createPerkItem(Material.CREEPER_HEAD, "Creeper Brawler Perk", "CREEPER_BRAWLER", 4, "Creeper Brawler Perk"))),
    HOT_HANDS(new HotHandsPerk(Items.createPerkItem(Material.BAKED_POTATO, "Hot Hands", "HOT_HANDS", 1, "Hot potato got a little too hot"))),
    GOLDEN_EXPERIENCE(new GoldenExperiencePerk(Items.createPerkSkull("http://textures.minecraft.net/texture/a5e048e5e94b945d161f0f3df83cc6f61985d5974c7ad9595197a50dc00edc0b",
            "Golden Experience", "GOLDEN_EXPERIENCE", 4, "Muda Muda Muda"))),
    DAMAGE_MULT(new DamageMultiplierPerk(Items.createPerkItem(Material.WOODEN_SWORD, "Damage Multiplier 1.5x", "DAMAGE_MULT", 4, "Deals an extra 1.5x damage"))),
    DAMAGE_TWO_MULT(new DamageMultiplierTwoPerk(Items.createPerkItem(Material.STONE_SWORD, "Damage Multiplier 2x", "DAMAGE_TWO_MULT", 4, "Deals an extra 2x damage"))),
    FLAT_DAMAGE(new FlatDamagePerk(Items.createPerkItem(Material.GLASS_PANE, "Flat Damage", "FLAT_DAMAGE", 4, "Deals an extra 2 flat damage"))),
    BLOODIED(new Bloodied(Items.createPerkItem(Material.REDSTONE, "Bloodied", "BLOODIED", 2, "When hp drops below 20/30/40% gain strength 1"), true)),
    FLASH(new Flash(Items.createPerkItem(Material.LEATHER_BOOTS, "Flash", "FLASH", 1, "Speed 1 but gain weakness every 3 minutes"))),
    JUMPER(new Jumper(Items.createPerkItem(Material.SLIME_BALL, "Pogo", "JUMPER", 1, "Jump Boost 1 but gain slowness every 1.5 minutes"))),
    UNYIELDING(new Resistant(Items.createPerkItem(Material.SHIELD, "Unyielding", "UNYIELDING", 2, "Resistance 1 when under 20/25/30% HP"), true)),
    LOW_MAINTENANCE(new LowMaintenance(Items.createPerkItem(Material.GOLDEN_CARROT, "Low Maintenance", "LOW_MAINTENANCE", 0, "Saturation 1 for 15 seconds every 2 minutes"))),
    KANGAROO(new Kangaroo(Items.createPerkItem(Material.RABBIT_FOOT, "Kangaroo", "KANGAROO", 3, "Double jump once every thirty seconds"),
            List.of(List.of(PerkType.FLASH, PerkType.JUMPER)))),
    THE_WORLD(new TheWorldPerk(Items.createPerkSkull("http://textures.minecraft.net/texture/ff1fc6ebc549c6da4807bd30fc6e47bf4bdb516f256864891a31e6f6aa2527b0",
            "The World", "THE_WORLD", 4, "The ultimate stando."))),
    SERENDIPITY(new Serendipity(Items.createPerkItem(Material.STRING, "Serendipity", "SERENDIPITY", 2, "20% chance to take no damage from mobs."))),
    OVERDRIVE(new Overdrive(Items.createPerkItem(Material.SPLASH_POTION, "Overdrive", "OVERDRIVE", 1, "Hit mobs with instant healing 1."))),
    BERSERKER(new Berserker(Items.createPerkItem(Material.IRON_AXE, "Berserker", "BERSERKER", 3, "Axe hits are multiplied by fist damage over last 3 seconds."))),
    COOKIE_CLICKER(new CookieClicker(Items.createPerkItem(Material.COOKIE, "Cookie Clicker", "COOKIE_CLICKER", 0, "Everytime you kill a player two cookies are dropped."))),
    BOUNTY_HUNTER(new BountyHunter(Items.createPerkItem(Material.CROSSBOW, "Bounty Hunter", "BOUNTY_HUNTER", 0, "Everytime you kill a player you gain money."))),
    XP_FARM(new XPFarm(Items.createPerkItem(Material.EXPERIENCE_BOTTLE, "XP Farm", "XP_FARM", 0, "Everytime you kill a player you gain xp."))),
    TAI_CHI(new TaiChi(Items.createPerkItem(Material.SPIDER_EYE, "Tai Chi", "TAI_CHI", 2, "Holding out your fist for three seconds and hitting an enemy inflicts wither with blindness."))),
    CONCUSSION(new Concussion(Items.createPerkItem(Material.BRICK, "Concussion", "CONCUSSION", 1, "Hitting a player with your fist gives them nausea."))),
    GHOST_FIST(new GhostFist(Items.createPerkItem(Material.GLOWSTONE_DUST, "Kumite", "GHOST_FIST", 3, "Hitting a player with your fist creates a delayed second hit."),
            List.of(List.of(PerkType.CONCUSSION), List.of(PerkType.TAI_CHI, PerkType.HOT_HANDS))));
    private final Perk perk;

    PerkType(Perk perk) {
        this.perk = perk;
    }

    public Perk getPerk() {
        return perk;
    }

    public ItemStack getItem() {
        return perk.getItem();
    }

    public static PerkType getPerkType(Perk perk) {
        for (PerkType type : PerkType.values()) {
            if (type.getPerk().getItem().equals(perk.getItem())) {
                return type;
            }
        }
        return null; // Return null if no match is found
    }

    public static List<PerkType> getPerksByRarity(int rarity) {
        List<PerkType> perks = new ArrayList<>();
        for (PerkType type: PerkType.values()) {
            if (Items.getRarity(type) == rarity)
                perks.add(type);
        }
        return perks;
    }

    public static List<PerkType> perkTypesByPage(int page) {
        List<PerkType> perks = new ArrayList<>();
        int count = 0, passed = 0;
        for (PerkType type: PerkType.values()) {
            if (Items.getRarity(type) != -1) { // Item is not hidden

                if (passed / 14 == page) {
                    perks.add(type);
                    count++;
                } else passed++;

                if (count == 14)
                    break;
            }
        }
        return perks;
    }

}
