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

Current Perks (Not Hidden):
- Bloodied
- Flash
- Jumper
- Kangaroo
- Low Maintenance
- Resistant

Future Plans:
- **Fix bug with creeper brawler**
- Add dependencies that perks can have on each other (Perk Extensions)
- Add Star Perks functionality to abstract class
- Make perks drop on death and go to the killer
- Players can scrap perks