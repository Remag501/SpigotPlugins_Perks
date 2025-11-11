# SpigotPlugins_Perks Version 1.0
Spigot plugin that handles the perk features for MC Battlegrounds server.

Usage for perks plugin:
- /perks - opens UI for perks menu
- /perks add - Displays the perks that can be added
- /perks add {PERKTYPE} - Adds a perk from the player running the command
- /perks add {Player} {PERKTYPE} - Adds a perk to the specified player
- /perks remove {PERKTYPE} - Removes a perk from the player running the command
- /perks remove {Player} {PERKTYPE} - Removes a perk to the specified player
- /perks hiddenui - Allows player to the see hidden perks

Features:
- UI to interact with available perks
- Each player can hold up to three perks
- Players can equip up to five perks at once
- Perks are saved on plugin shutdown
- Perks can only work in specific worlds (Currently Sahara, Icycaverns, Kuroko, Musicland, Thundra)
- Perks have different rarities

Current Perks (Not up to date):
- Bloodied
- Flash
- Jumper
- Kangaroo
- Low Maintenance
- Resistant

## Known Issues
*   **Bug 1: Berserker**
    *   **Description:** Allows infinite damage stacking (3 second timer not working)
*   **Bug 2: Hiddenui**
    *   **Description:** Hidden ui does not work due to current display method
*   **Improvement needed: Active Perk List**
    *   **Description:** Every perk uses a active perk list but its not in interface

## Refactor plans: 
- Prevent perk instances from storing unneeded data, this started with itemstack
- There is multiple data to be refactored (Itemstack, requirements, stars)
- Handling of functions getPerk(), getItem(), and getPerkType() from PerkType contributes to major refactor
- We can use information in enum to create objects. For getPerkType I have to explore different options
- Implementation Idea: Store PerkType in Perk, and pass by constructor for every perk to define it.
- Every perk class needs to be adjusted for this implementation
- Every calling of perkType enums, have to be adjusted
- Considering readdressing the implementation of activate/deactivate player
- Implementation idea: 1. leverage PlayerPerks 2. call enable within activate (flaw: activate may not always not be to called when enabled)
- Remove PlayerPerkInstance (no need and confusion)

