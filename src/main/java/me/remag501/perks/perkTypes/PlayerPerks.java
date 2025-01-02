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
    private Set<PlayerPerkInstance> equippedPerks;
    private List<Perk> ownedPerks;

    public static PlayerPerks getPlayerPerks(UUID playerUUID) {
        return playersPerks.get(playerUUID);
    }

    public void loadPerks(Player player) {
        String playerID = player.getUniqueId().toString();
        ConfigUtil perkConfig = new ConfigUtil(Bukkit.getPluginManager().getPlugin("Perks"), "perks.yml");
        List<String> equippedPerks = perkConfig.getConfig().getStringList(playerID + "_equipped");
        List<String> ownedPerks = perkConfig.getConfig().getStringList(playerID + "_owned");
        // Convert to Perk ArrayList and set this update this instance to existing perks
        for (String perk: equippedPerks)
            this.equippedPerks.add(new PlayerPerkInstance(PerkType.valueOf(perk), player));
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
            for (PlayerPerkInstance perk: playerPerk.equippedPerks) {
                save.add(Items.getPerkID(perk.getPerkType().getItem()));
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

    // Scuffed due to previous versions
    public List<Perk> getEquippedPerks() {
        // Returns list for ease of displaying
        ArrayList<Perk> rv = new ArrayList<>();
        for (PlayerPerkInstance instance: equippedPerks) {
            rv.add(instance.getPerkType().getPerk());
        }
        return rv;
    }

    public List<Perk> getOwnedPerks() {
        return ownedPerks;
    }

    public PlayerPerks(UUID playerUUID) {
        equippedPerks = new HashSet<PlayerPerkInstance>();
        ownedPerks = new ArrayList<Perk>();
        playersPerks.put(playerUUID, this);
        this.playerUUID = playerUUID;
        if (playerUUID != null)
            loadPerks(Bukkit.getPlayer(playerUUID));
    }

    public boolean removeEquippedPerk(PerkType perkType) {
        PlayerPerkInstance perkInstance = null;
        for (PlayerPerkInstance instance : equippedPerks) {
            if (instance.getPerkType().equals(perkType)) {
                perkInstance = instance;
                break;
            }
        }
        if (perkInstance == null) return false;
        // Disable the perk before removing it
        Perk perk = perkInstance.getPerkType().getPerk();
        equippedPerks.remove(perkInstance);
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
        Player player = Bukkit.getPlayer(playerUUID);
        if (equippedPerks.size() >= 5)
            return false;
        for (PlayerPerkInstance perkInstance : equippedPerks) {
            if (perkInstance.getPerkType().equals(perkType))
                return false; // Perk already equipped
        }
        // Add perks to set
        PlayerPerkInstance perkInstance = new PlayerPerkInstance(perkType, player);
        equippedPerks.add(perkInstance);
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
        perkInstance.getPerkType().getPerk().onEnable(player);
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
