package me.remag501.perks;

import me.remag501.perks.commands.PerksCommand;
import me.remag501.perks.core.Perk;
import me.remag501.perks.core.PerkType;
import me.remag501.perks.core.PlayerPerks;
import me.remag501.perks.utils.GambleUI;
import me.remag501.perks.listeners.PerkChangeListener;
import me.remag501.perks.utils.ScrapUI;
import me.remag501.perks.utils.UI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Perks extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getLogger().info("Perks has started up!");
        // Add commands to the plugin
        getCommand("perks").setExecutor(new PerksCommand(this));
        getServer().getPluginManager().registerEvents(new UI(null, false), this);
        getServer().getPluginManager().registerEvents(new GambleUI(), this);
        getServer().getPluginManager().registerEvents(new PerkChangeListener(), this);
        getServer().getPluginManager().registerEvents(new ScrapUI(), this);
        // Enable worlds for the plugin
        PerkChangeListener.enabledWorlds.add("sahara");
        PerkChangeListener.enabledWorlds.add("icycaverns");
        PerkChangeListener.enabledWorlds.add("kuroko");
        PerkChangeListener.enabledWorlds.add("musicland");
        PerkChangeListener.enabledWorlds.add("thundra");
        PerkChangeListener.enabledWorlds.add("test");
        // Enable listerners for perks
        getServer().getPluginManager().registerEvents((Listener) PerkType.HOT_HANDS.getPerk(), this);
        getServer().getPluginManager().registerEvents((Listener) PerkType.BLOODIED.getPerk(), this);
        getServer().getPluginManager().registerEvents((Listener) PerkType.KANGAROO.getPerk(), this);
        getServer().getPluginManager().registerEvents((Listener) PerkType.UNYIELDING.getPerk(), this);
        getServer().getPluginManager().registerEvents((Listener) PerkType.THE_WORLD.getPerk(), this);
        getServer().getPluginManager().registerEvents((Listener) PerkType.SERENDIPITY.getPerk(), this);
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
