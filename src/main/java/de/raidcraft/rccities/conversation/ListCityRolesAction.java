package de.raidcraft.rccities.conversation;

import de.raidcraft.api.action.action.Action;
import de.raidcraft.api.conversations.answer.Answer;
import de.raidcraft.api.conversations.conversation.Conversation;
import de.raidcraft.api.conversations.stage.Stage;
import de.raidcraft.rccities.api.resident.Role;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Philip Urban
 */
public class ListCityRolesAction implements Action<Conversation> {

    @Override
    @Information(
            value = "city.list-roles",
            desc = "Lists all city roles.",
            conf = {
                    "text: to display",
                    "next-stage: stage to process selected role with"
            },
            aliases = "LIST_CITY_ROLES"
    )
    public void accept(Conversation conversation, ConfigurationSection config) {

        Stage stage = Stage.of(conversation, config.getString("text"));
        for (Role role : Role.values()) {
            String crossed = (role.isAdminOnly() && !conversation.getOwner().hasPermission("rccities.resident.promote.all"))
                    ? ChatColor.RED + ChatColor.STRIKETHROUGH.toString() : ChatColor.GREEN.toString();
            Answer answer = Answer.of(crossed + role.getFriendlyName());
            answer.addAction(Action.setConversationVariable("role", role.name()));
            answer.addAction(Action.changeStage(config.getString("next-stage", "next")));
            stage.addAnswer(answer);
        }

        stage.changeTo();
    }
}
