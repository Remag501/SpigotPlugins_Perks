package me.remag501.perks.perkTypes;

import me.remag501.perks.Perks;
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
//    private Set<PlayerPerkInstance> equippedPerks;
//    private List<Perk> ownedPerks;
    private Map<PerkType, Perk> equippedPerks;
    private Map<PerkType, Perk> ownedPerks;

    public static PlayerPerks getPlayerPerks(UUID playerUUID) {
        return playersPerks.get(playerUUID);
    }

    public void loadPerks(Player player) {
        String playerID = player.getUniqueId().toString();
        ConfigUtil perkConfig = new ConfigUtil(Bukkit.getPluginManager().getPlugin("Perks"), "perks.yml");
        List<String> equippedPerks = perkConfig.getConfig().getStringList(playerID + "_equipped");
        List<String> ownedPerks = perkConfig.getConfig().getStringList(playerID + "_owned");
        // Convert to Perk ArrayList and set this update this instance to existing perks
        for (String perk: ownedPerks) // Owned Perks have to be called first
            this.addOwnedPerks(PerkType.valueOf(perk));
        for (String perk: equippedPerks)
            this.addEquippedPerk(PerkType.valueOf(perk));
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
            for (Perk perk: playerPerk.getEquippedPerks()) {
                save.add(Items.getPerkID(perk.getItem()));
            }
            perkConfig.set(playerIDString + "_equipped", save);
            // Add owned perks to config
            save = new ArrayList<>();
            for (Perk perk: playerPerk.getOwnedPerks())
                save.add(Items.getPerkID(perk.getItem()));
            perkConfig.set(playerIDString + "_owned", save);
        }
        // Save the config to the file
        perkConfigUtil.save();
    }

    // Return list for ease of UI handling, requires optimization in future
    public List<Perk> getEquippedPerks() {
        return new ArrayList<>(equippedPerks.values()); // Keys and values are same amount, values easier since they are perk object already
    }

    // Return list for ease of UI handling, requires optimization in future
    public List<Perk> getOwnedPerks() {
        ArrayList<Perk> rv = new ArrayList<Perk>();
        for (Perk perkInstance: ownedPerks.values()) { // Values contain the quantities
            for (int i = 0; i < perkInstance.getQuantity(); i++)
                rv.add(perkInstance); // Should work with clone of object due to having same ItemStack pointer
        }
        return rv;
    }

    public PlayerPerks(UUID playerUUID) {
        equippedPerks = new HashMap<PerkType, Perk>();
        ownedPerks = new HashMap<PerkType, Perk>();
        playersPerks.put(playerUUID, this);
        this.playerUUID = playerUUID;
        if (playerUUID != null)
            loadPerks(Bukkit.getPlayer(playerUUID));
    }

    public boolean removeEquippedPerk(PerkType perkType) {
        Perk perk = equippedPerks.get(perkType); // Check if perk is equipped
        if (perk == null) return false;
        // Disable the perk before removing it
        equippedPerks.remove(perkType);
        // Message the player
        Player player = Bukkit.getPlayer(playerUUID);
        player.sendMessage("§cYou have deequipped the perk " + perkType.getItem().getItemMeta().getDisplayName());
        // Check if they are in the correct world before disabling
        boolean inWorld = false;
        for (String world: PerkChangeListener.enabledWorlds) {
            if (Bukkit.getPlayer(playerUUID).getWorld().getName().equalsIgnoreCase(world)) {
                inWorld = true;
                break;
            }
        }
        if (!inWorld)
            return true;
        perk.onDisable();
        return true;
    }

    public boolean addEquippedPerk(PerkType perkType) {
        Player player = Bukkit.getPlayer(playerUUID);
        if (equippedPerks.size() >= 5)
            return false;
        Perk perkInstance = equippedPerks.get(perkType);
        // Check if perk is already equipped
        if (perkInstance != null)
            return false;
        // Add perks to set
        perkInstance = ownedPerks.get(perkType);
        if (perkInstance == null) return false; // Just in case
        equippedPerks.put(perkType, perkInstance);
        player.sendMessage("§2You have equipped the perk " + perkType.getItem().getItemMeta().getDisplayName());
        // Only enables if the player is in the correct world
        boolean inWorld = false;
        for (String world: PerkChangeListener.enabledWorlds) {
            if (Bukkit.getPlayer(playerUUID).getWorld().getName().equalsIgnoreCase(world)) {
                inWorld = true;
                break;
            }
        }
        if (!inWorld)
            return true; // Equipped but not enabled
        perkInstance.onEnable();
        return true;
    }

    public boolean addOwnedPerks(PerkType perkType) {
        // O(n) complexity
        Player player = Bukkit.getPlayer(playerUUID);
        Perk perkInstance = ownedPerks.get(perkType);
        if (perkInstance != null) { // Perk already owned
            return perkInstance.addCount();
        }
        else {
            Bukkit.getPluginManager().getPlugin("Perks").getLogger().info("Reached, Player object is " + player.toString());
            perkInstance = perkType.getPerk().clone();
            perkInstance.setPlayer(player);
            ownedPerks.put(perkType, perkInstance);
            return true;
        }
    }

    public void removeOwnedPerk(PerkType perkType) {
        // Doesnt need count checker since remove leaves array the same if the value is not found
        // O(1) complexity
        if (ownedPerks.get(perkType) == null)
            return; // No perk found
        Perk perkInstance = ownedPerks.get(perkType);
        if (perkInstance.getQuantity() > 1)
            perkInstance.lowerCount();
        else { // Removes from HashMap and unequips
            ownedPerks.remove(perkType);
            removeEquippedPerk(perkType);
        }
    }
}
