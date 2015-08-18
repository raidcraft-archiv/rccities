package de.raidcraft.rccities.actions;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.api.action.action.Action;
import de.raidcraft.api.conversations.conversation.ConversationVariable;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
public class ShowCityInfoAction implements Action<Player> {

    @Override
    @Information(
            value = "city.show-info",
            desc = "Shows information about the city.",
            conf = "city",
            aliases = "SHOW_CITY_INFO"
    )
    public void accept(Player player, ConfigurationSection config) {

        String cityName = ConversationVariable.getString(player, "city").orElse(config.getString("city"));

        City city = RaidCraft.getComponent(RCCitiesPlugin.class).getCityManager().getCity(cityName);
        if (city == null) {
            player.sendMessage(ChatColor.RED + "City '" + cityName + "' does not exist!");
            return;
        }

        try {
            RaidCraft.getComponent(RCCitiesPlugin.class).getCityManager().printCityInfo(city.getName(), player);
        } catch (RaidCraftException ignored) {
            // should never occur since we already checked for a valid city
        }
    }
}
