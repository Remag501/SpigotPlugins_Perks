package me.remag501.perks;

import me.remag501.perks.commands.PerksCommand;
import me.remag501.perks.perkTypes.Perk;
import me.remag501.perks.perkTypes.PlayerPerks;
import me.remag501.perks.utils.PerkChangeListener;
import me.remag501.perks.utils.UI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Perks extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getLogger().info("Perks has started up!");
        // Add commands to the plugin
        getCommand("perks").setExecutor(new PerksCommand(this));
        Bukkit.getPluginManager().registerEvents(new UI(null, false), this);
        Bukkit.getPluginManager().registerEvents(new PerkChangeListener(), this);
    }

    @Override
    public void onDisable() {
        // Disable all perks enabled for every player
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Perk perk : PlayerPerks.getPlayerPerks(player.getUniqueId()).getEquippedPerks()) {
                perk.onDisable(player);
            }
        }
    }
}
