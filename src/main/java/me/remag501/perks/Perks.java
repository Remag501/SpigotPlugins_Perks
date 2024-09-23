package me.remag501.perks;

import me.remag501.perks.commands.PerksCommand;
import me.remag501.perks.utils.UI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Perks extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getLogger().info("Perks has started up!");
        // Add commands to the plugin
        getCommand("perks").setExecutor(new PerksCommand(this));
        Bukkit.getPluginManager().registerEvents(new UI(null), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
