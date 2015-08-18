package de.raidcraft.rccities.actions;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.action.action.Action;
import de.raidcraft.api.conversations.Conversations;
import de.raidcraft.api.conversations.conversation.ConversationEndReason;
import de.raidcraft.api.conversations.conversation.ConversationVariable;
import de.raidcraft.api.economy.AccountType;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
public class ShowCityFlowAction implements Action<Player> {

    @Override
    @Information(
            value = "city.show-flow",
            desc = "Shows the bank account flow for the city.",
            conf = {
                    "entries: to show - defaults to 10",
                    "city: to show flow for"
            },
            aliases = "SHOW_CITY_FLOW"
    )
    public void accept(Player player, ConfigurationSection config) {

        String cityName = ConversationVariable.getString(player, "city").orElse(config.getString("city"));
        int entries = config.getInt("entries", 10);

        City city = RaidCraft.getComponent(RCCitiesPlugin.class).getCityManager().getCity(cityName);
        if (city == null) {
            player.sendMessage(ChatColor.RED + "Ungültige Stadt ausgewählt!");
            Conversations.endActiveConversation(player, ConversationEndReason.ERROR);
            return;
        }

        RaidCraft.getEconomy().printFlow(player, AccountType.CITY, city.getBankAccountName(), entries);
    }
}
