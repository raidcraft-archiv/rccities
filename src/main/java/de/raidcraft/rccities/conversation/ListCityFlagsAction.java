package de.raidcraft.rccities.conversation;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.action.action.Action;
import de.raidcraft.api.conversations.answer.Answer;
import de.raidcraft.api.conversations.conversation.Conversation;
import de.raidcraft.api.conversations.stage.Stage;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.flags.FlagInformation;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.stream.Collectors;

public class ListCityFlagsAction implements Action<Conversation> {

    @Override
    @Information(
            value = "city.list-flags",
            desc = "Lists all city flags.",
            conf = {
                    "text: to display",
                    "next-stage: stage to process selected flag with",
                    "admin-flags: false - true to display admin flags also"
            },
            aliases = "LIST_CITY_FlAGS"
    )
    public void accept(Conversation conversation, ConfigurationSection config) {


        List<FlagInformation> flagInformationList = RaidCraft.getComponent(RCCitiesPlugin.class).getFlagManager()
                .getRegisteredCityFlagInformationList()
                .stream()
                .filter(flagInformation -> config.getBoolean("admin-flags", false) || !flagInformation.adminOnly())
                .collect(Collectors.toList());

        Stage stage = Stage.of(conversation, config.getString("text"));
        for (FlagInformation flagInformation : flagInformationList) {
            Answer answer = Answer.of(flagInformation.friendlyName());
            answer.addAction(Action.setConversationVariable("city_flag", flagInformation.name()));
            answer.addAction(Action.setConversationVariable("city_flag_friendlyname", flagInformation.friendlyName()));
            answer.addAction(Action.changeStage(config.getString("next-stage", "next")));
            stage.addAnswer(answer);
        }

        stage.changeTo();
    }
}
