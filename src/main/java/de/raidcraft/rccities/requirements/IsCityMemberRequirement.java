package de.raidcraft.rccities.requirements;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.action.requirement.Requirement;
import de.raidcraft.api.conversations.Conversations;
import de.raidcraft.api.conversations.conversation.ConversationVariable;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.manager.CityManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class IsCityMemberRequirement implements Requirement<Player> {

    @Override
    @Information(
            value = "city.is-member",
            desc = "Checks if the player is a member of the given city.",
            conf = {
                    "city: to check"
            },
            aliases = {"IS_CITY_MEMBER"}
    )
    public boolean test(Player player, ConfigurationSection config) {

        String cityName = ConversationVariable.getString(player, "city").orElse(config.getString("city"));
        City city = RaidCraft.getComponent(CityManager.class).getCity(cityName);
        if (city == null) {
            player.sendMessage(ChatColor.RED + "Ungültige Stadt mit dem Namen " + cityName + " angegeben!");
            Conversations.changeStage(player, config.getString("onfailure"));
            return false;
        }
        Conversations.changeStage(player, config.getString("onsuccess"));
        return city.isMember(player);
    }
}
