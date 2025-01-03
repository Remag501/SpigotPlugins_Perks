package me.remag501.perks;

import me.remag501.perks.commands.PerksCommand;
import me.remag501.perks.perkTypes.Perk;
import me.remag501.perks.perkTypes.PerkType;
import me.remag501.perks.perkTypes.PlayerPerks;
import me.remag501.perks.utils.PerkChangeListener;
import me.remag501.perks.utils.UI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

public final class Perks extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getLogger().info("Perks has started up!");
        // Add commands to the plugin
        getCommand("perks").setExecutor(new PerksCommand(this));
        Bukkit.getPluginManager().registerEvents(new UI(null, false), this);
        Bukkit.getPluginManager().registerEvents(new PerkChangeListener(), this);
        // Enable worlds for the plugin
        PerkChangeListener.enabledWorlds.add("Sahara");
        PerkChangeListener.enabledWorlds.add("Icycaverns");
        PerkChangeListener.enabledWorlds.add("Kuroko");
        PerkChangeListener.enabledWorlds.add("Musicland");
        PerkChangeListener.enabledWorlds.add("Thundra");

        getServer().getPluginManager().registerEvents((Listener) PerkType.HOT_HANDS.getPerk(), this);
        getServer().getPluginManager().registerEvents((Listener) PerkType.BLOODIED.getPerk(), this);
        getServer().getPluginManager().registerEvents((Listener) PerkType.KANGAROO.getPerk(), this);
        getServer().getPluginManager().registerEvents((Listener) PerkType.RESISTANT.getPerk(), this);
    }

    @Override
    public void onDisable() {
        // Disable all perks enabled for every player
        PlayerPerks.savePerks();
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Perk perk : PlayerPerks.getPlayerPerks(player.getUniqueId()).getEquippedPerks()) {
                perk.onDisable();
            }
        }
    }
}
