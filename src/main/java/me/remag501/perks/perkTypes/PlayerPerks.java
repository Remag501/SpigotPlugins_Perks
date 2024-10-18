package me.remag501.perks.perkTypes;

import me.remag501.perks.utils.ConfigUtil;
import me.remag501.perks.utils.Items;
import me.remag501.perks.utils.PerkChangeListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerPerks {

    private static HashMap<UUID, PlayerPerks> playersPerks = new HashMap<>();
    private UUID playerUUID;
    private List<Perk> equippedPerks;
    private List<Perk> ownedPerks;

    public static PlayerPerks getPlayerPerks(UUID playerUUID) {
        return playersPerks.get(playerUUID);
    }

    public void loadPerks(String playerID) {
        ConfigUtil perkConfig = new ConfigUtil(Bukkit.getPluginManager().getPlugin("Perks"), "perks.yml");
        List<String> equippedPerks = perkConfig.getConfig().getStringList(playerID + "_equipped");
        List<String> ownedPerks = perkConfig.getConfig().getStringList(playerID + "_owned");
        // Convert to Perk ArrayList and set this update this instance to existing perks
        for (String perk: equippedPerks)
            this.equippedPerks.add(PerkType.valueOf(perk).getPerk());
        for (String perk: ownedPerks)
            this.ownedPerks.add(PerkType.valueOf(perk).getPerk());
    }

    public static void savePerks() {
        ConfigUtil perkConfigUtil = new ConfigUtil(Bukkit.getPluginManager().getPlugin("Perks"), "perks.yml");
        FileConfiguration perkConfig = perkConfigUtil.getConfig();
        // Iterate through set of perks for each player
        for (UUID playerID: playersPerks.keySet()) {
            PlayerPerks playerPerk = playersPerks.get(playerID);
            String playerIDString = playerID.toString();
            // Convert to list to set in the config file
            List<String> save = new ArrayList<>();
            for (Perk perk: playerPerk.equippedPerks) {
                save.add(Items.getPerkID(perk.getItem()));
            }
            perkConfig.set(playerIDString + "_equipped", save);
            // Add owned perks to config
            save = new ArrayList<>();
            for (Perk perk: playerPerk.ownedPerks)
                save.add(Items.getPerkID(perk.getItem()));
            perkConfig.set(playerIDString + "_owned", save);
        }
        // Save the config to the file
        perkConfigUtil.save();
    }

    public List<Perk> getEquippedPerks() {
        return equippedPerks;
    }

    public List<Perk> getOwnedPerks() {
        return ownedPerks;
    }

    public PlayerPerks(UUID playerUUID) {
        equippedPerks = new ArrayList<Perk>();
        ownedPerks = new ArrayList<Perk>();
        playersPerks.put(playerUUID, this);
        this.playerUUID = playerUUID;
        if (playerUUID != null)
            loadPerks(Bukkit.getPlayer(playerUUID).getUniqueId().toString());
    }


    public boolean removeEquippedPerk(PerkType perkType) {
        if (!equippedPerks.contains(perkType.getPerk()))
            return false;
        // Disable the perk before removing it
        Perk perk = perkType.getPerk();
        equippedPerks.remove(perk);
        boolean inWorld = false;
        for (String world: PerkChangeListener.enabledWorlds) {
            if (Bukkit.getPlayer(playerUUID).getWorld().getName().equalsIgnoreCase(world)) {
                inWorld = true;
                break;
            }
        }
        if (!inWorld)
            return true;
        Player player = Bukkit.getPlayer(playerUUID);
        perk.onDisable(player);
        player.sendMessage("§cYou have deequipped the perk " + perkType.getItem().getItemMeta().getDisplayName());
        return true;
    }

    public boolean addEquippedPerk(PerkType perkType) {
        if (equippedPerks.contains(perkType.getPerk()))
            return false;
        if (equippedPerks.size() >= 5)
            return false;
        // Enable the perk before adding it
        equippedPerks.add(perkType.getPerk());
        // Only enables if the player is not in spawn
        boolean inWorld = false;
        for (String world: PerkChangeListener.enabledWorlds) {
            if (Bukkit.getPlayer(playerUUID).getWorld().getName().equalsIgnoreCase(world)) {
                inWorld = true;
                break;
            }
        }
        if (!inWorld)
            return true;
        Player player = Bukkit.getPlayer(playerUUID);
        perkType.getPerk().onEnable(player);
        player.sendMessage("§2You have equipped the perk " + perkType.getItem().getItemMeta().getDisplayName());
        return true;
    }

    public boolean addOwnedPerks(PerkType perkType) {
        // O(n) complexity
        int count = Collections.frequency(ownedPerks, perkType.getPerk());
        if (count < 3) {
            ownedPerks.add(perkType.getPerk());
            return true;
        }
        return false;
    }

    public void removeOwnedPerk(PerkType perkType) {
        // Doesnt need count checker since remove leaves array the same if the value is not found
        // O(1) complexity
//        Bukkit.getPluginManager().getPlugin("Perks").getLogger().info(getEquippedPerks().toString());
//        Bukkit.getPluginManager().getPlugin("Perks").getLogger().info(getOwnedPerks().toString());
        if (!ownedPerks.contains(perkType.getPerk()))
            return;
        ownedPerks.remove(perkType.getPerk());
        // Checks if the owned perks are zero, if so unequips
        if (!ownedPerks.contains(perkType.getPerk()))
            removeEquippedPerk(perkType);
    }
}
