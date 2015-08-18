package de.raidcraft.rccities.conversation;

import de.raidcraft.api.action.action.Action;
import de.raidcraft.api.config.builder.ConfigGenerator;
import de.raidcraft.api.conversations.answer.Answer;
import de.raidcraft.api.conversations.conversation.Conversation;
import de.raidcraft.api.conversations.conversation.ConversationEndReason;
import de.raidcraft.api.conversations.stage.Stage;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rcupgrades.api.upgrade.Upgrade;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

/**
 * @author Philip Urban
 */
public class ListUpgradeTypesAction implements Action<Conversation> {

    @Override
    @ConfigGenerator.Information(
            value = "city.list-upgrade-types",
            desc = "Lists all city upgrade types.",
            conf = {
                    "text: to display",
                    "next-stage: stage to process selected upgrade type with"
            },
            aliases = "LIST_CITY_UPGRADE_TYPES"
    )
    public void accept(Conversation conversation, ConfigurationSection config) {

        if (!(conversation instanceof CityConversation)) {
            conversation.sendMessage(ChatColor.RED + "Conversation must be of type city conversation to list join requests.");
            conversation.abort(ConversationEndReason.ERROR);
            return;
        }

        City city = ((CityConversation) conversation).getCity();
        List<Upgrade> upgrades = city.getUpgrades().getUpgrades();
        Stage stage = Stage.of(conversation, config.getString("text"));

        for (Upgrade upgrade : upgrades) {
            Answer answer = Answer.of(upgrade.getName());
            answer.addAction(Action.setConversationVariable("city_upgrade_type", upgrade.getId()));
            answer.addAction(Action.setConversationVariable("city_upgrade_type_name", upgrade.getName()));
            answer.addAction(Action.changeStage(config.getString("next-stage", "next")));
            stage.addAnswer(answer);
        }

        stage.changeTo();
    }
}
