package de.raidcraft.rccities.conversation;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.api.conversations.conversation.Conversation;
import de.raidcraft.api.conversations.conversation.ConversationVariable;
import de.raidcraft.conversations.answers.InputAnswer;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.flags.FlagInformation;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Philip Urban
 */
public class SetCityFlagInputAnswer extends InputAnswer {

    public SetCityFlagInputAnswer(String type, ConfigurationSection config) {

        super(type, config);
    }

    @Override
    public boolean processInput(Conversation conversation, String input) {

        if (!(conversation instanceof CityConversation)) {
            conversation.sendMessage(ChatColor.RED + "Conversation must be a city conversation!");
            return false;
        }

        City city = ((CityConversation) conversation).getCity();
        String flagName = ConversationVariable.getString(conversation.getOwner(), "city_flag").orElse(null);

        try {
            FlagInformation flagInformation = RaidCraft.getComponent(RCCitiesPlugin.class).getFlagManager().getRegisteredCityFlagInformation(flagName);
            RaidCraft.getComponent(RCCitiesPlugin.class).getFlagManager().setCityFlag(city, conversation.getOwner(), flagName, input);
            conversation.sendMessage(ChatColor.GREEN + "Du hast erfolgreich die Flag '" + ChatColor.YELLOW + flagInformation.friendlyName()
                    + ChatColor.GREEN + "' auf den Wert '" + ChatColor.YELLOW + input.toUpperCase() + ChatColor.GREEN + "' gesetzt!");
            return true;
        } catch (NullPointerException | RaidCraftException e) {
            conversation.sendMessage(ChatColor.RED + "Fehler beim ändern der Flag: " + e.getMessage());
            conversation.sendMessage(ChatColor.RED + e.getMessage());
            return false;
        }
    }
}
