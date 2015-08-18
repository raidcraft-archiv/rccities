package de.raidcraft.rccities.conversation;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.conversations.conversation.Conversation;
import de.raidcraft.conversations.answers.InputAnswer;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.resident.Resident;
import de.raidcraft.util.UUIDUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Philip Urban
 */
public class FindCityResidentInputAnswer extends InputAnswer {

    public FindCityResidentInputAnswer(String type, ConfigurationSection config) {

        super(type, config);
    }

    @Override
    public boolean processInput(Conversation conversation, String input) {

        if (!(conversation instanceof CityConversation)) {
            conversation.sendMessage(ChatColor.RED + "Conversation must be a city conversation!");
            return false;
        }

        City city = ((CityConversation) conversation).getCity();
        Resident resident = RaidCraft.getComponent(RCCitiesPlugin.class).getResidentManager().getResident(UUIDUtil.convertPlayer(input), city);

        if (resident == null) {
            conversation.sendMessage(ChatColor.RED + "Es wurde kein Bürger mit dem Namen " + input + " gefunden! Bitte versuche es noch einmal...");
            return false;
        } else {
            conversation.set("resident_name", resident.getName());
            return true;
        }
    }
}
