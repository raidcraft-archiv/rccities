package de.raidcraft.rccities.conversation;

import de.raidcraft.api.action.action.Action;
import de.raidcraft.api.conversations.answer.Answer;
import de.raidcraft.api.conversations.conversation.Conversation;
import de.raidcraft.api.conversations.conversation.ConversationEndReason;
import de.raidcraft.api.conversations.stage.Stage;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.request.JoinRequest;
import de.raidcraft.util.UUIDUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

/**
 * @author Philip Urban
 */
public class ListJoinRequestsAction implements Action<Conversation> {

    @Override
    @Information(
            value = "city.list-join-requests",
            desc = "Lists all city join requests.",
            conf = {
                    "text: to display",
                    "next-stage: stage to process selected join request with"
            },
            aliases = "LIST_CITY_JOIN_REQUESTS"
    )
    public void accept(Conversation conversation, ConfigurationSection config) {

        if (!(conversation instanceof CityConversation)) {
            conversation.sendMessage(ChatColor.RED + "Conversation must be of type city conversation to list join requests.");
            conversation.abort(ConversationEndReason.ERROR);
            return;
        }

        City city = ((CityConversation) conversation).getCity();

        List<JoinRequest> joinRequests = city.getJoinRequests();
        if (joinRequests.isEmpty()) {
            conversation.sendMessage(ChatColor.RED + "Keine Anträge verfügbar!");
            return;
        }

        Stage stage = Stage.of(conversation, config.getString("text"));

        for (JoinRequest joinRequest : joinRequests) {
            String crossed = (joinRequest.isRejected()) ? ChatColor.RED + ChatColor.STRIKETHROUGH.toString() : ChatColor.GREEN.toString();
            String playerName = UUIDUtil.getNameFromUUID(joinRequest.getPlayer());
            Answer answer = Answer.of(crossed + playerName);
            answer.addAction(Action.setConversationVariable("join_request", playerName));
            answer.addAction(Action.changeStage(config.getString("next-stage", "next")));
            stage.addAnswer(answer);
        }

        stage.changeTo();
    }
}
