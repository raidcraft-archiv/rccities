package de.raidcraft.rccities.requirements;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.action.requirement.Requirement;
import de.raidcraft.api.conversations.Conversations;
import de.raidcraft.api.conversations.conversation.ConversationVariable;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.resident.Role;
import de.raidcraft.rccities.manager.CityManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class HasCityRoleRequirement implements Requirement<Player> {

    @Override
    @Information(
            value = "city.has-role",
            desc = "Checks if the player has the given role inside the city.",
            conf = {
                    "role: role to check",
                    "city: to check"
            },
            aliases = {"HAS_CITY_ROLE"}
    )
    public boolean test(Player player, ConfigurationSection config) {

        String cityName = ConversationVariable.getString(player, "city").orElse(config.getString("city"));
        City city = RaidCraft.getComponent(CityManager.class).getCity(cityName);
        if (city == null) {
            player.sendMessage(ChatColor.RED + "Ungültige Stadt mit dem Namen " + cityName + " angegeben!");
            Conversations.changeStage(player, config.getString("onfailure"));
            return false;
        }

        if (city.hasRole(player, Role.valueOf(config.getString("role")))) {
            Conversations.changeStage(player, config.getString("onsuccess"));
            return true;
        } else {
            Conversations.changeStage(player, config.getString("onfailure"));
            return false;
        }
    }
}
