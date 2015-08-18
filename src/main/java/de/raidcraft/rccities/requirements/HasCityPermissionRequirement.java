package de.raidcraft.rccities.requirements;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.action.requirement.Requirement;
import de.raidcraft.api.conversations.Conversations;
import de.raidcraft.api.conversations.conversation.ConversationVariable;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.resident.RolePermission;
import de.raidcraft.rccities.manager.CityManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class HasCityPermissionRequirement implements Requirement<Player> {

    @Override
    @Information(
            value = "city.has-permission",
            desc = "Checks if the player has the given role permission inside the city.",
            conf = {
                    "permission: role permission to check",
                    "city: to check"
            },
            aliases = {"HAS_CITY_PERMISSION"}
    )
    public boolean test(Player player, ConfigurationSection config) {

        String cityName = ConversationVariable.getString(player, "city").orElse(config.getString("city"));
        City city = RaidCraft.getComponent(CityManager.class).getCity(cityName);
        if (city == null) {
            player.sendMessage(ChatColor.RED + "Ungültige Stadt mit dem Namen " + cityName + " angegeben!");
            Conversations.changeStage(player, config.getString("onfailure"));
            return false;
        }

        if (city.hasRolePermission(player, RolePermission.valueOf(config.getString("permission")))) {
            Conversations.changeStage(player, config.getString("onsuccess"));
            return true;
        } else {
            Conversations.changeStage(player, config.getString("onfailure"));
            return false;
        }
    }
}
