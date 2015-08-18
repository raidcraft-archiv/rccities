package de.raidcraft.rccities.conversation;

import de.raidcraft.api.action.action.Action;
import de.raidcraft.api.conversations.answer.Answer;
import de.raidcraft.api.conversations.conversation.Conversation;
import de.raidcraft.api.conversations.conversation.ConversationEndReason;
import de.raidcraft.api.conversations.stage.Stage;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rcupgrades.api.level.UpgradeLevel;
import de.raidcraft.rcupgrades.api.upgrade.Upgrade;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip Urban
 */
public class ListUpgradeLevelAction implements Action<Conversation> {

    @Override
    @Information(
            value = "city.list-upgrade-levels",
            desc = "Lists all city upgrade levels.",
            conf = {
                    "text: to display",
                    "next-stage: stage to process selected upgrade level with",
                    "city_upgrade_type: to list levels for"
            },
            aliases = "LIST_CITY_UPGRADE_LEVEL"
    )
    public void accept(Conversation conversation, ConfigurationSection config) {

        if (!(conversation instanceof CityConversation)) {
            conversation.sendMessage(ChatColor.RED + "Conversation must be of type city conversation to list join requests.");
            conversation.abort(ConversationEndReason.ERROR);
            return;
        }

        City city = ((CityConversation) conversation).getCity();

        Upgrade upgrade = city.getUpgrades().getUpgrade(config.getString("city_upgrade_type"));
        if (upgrade == null) {
            conversation.sendMessage(ChatColor.RED + "Upgrade '" + config.getString("upgrade-type") + "' does not exist!");
            conversation.abort(ConversationEndReason.ERROR);
            return;
        }

        List<UpgradeLevel> levels = upgrade.getLevels();
        // delete not reachable levels
        UpgradeLevel highestLockedLevel = upgrade.getLowestLockedLevel();
        new ArrayList<>(levels).stream()
                .filter(level -> highestLockedLevel != null
                        && level.getLevel() > highestLockedLevel.getLevel()
                        && level.isStored())
                .forEach(levels::remove);

        Stage stage = Stage.of(conversation, config.getString("text"));

        for (UpgradeLevel level : levels) {
            String crossed = (level.isUnlocked() && level.isStored()) ? ChatColor.RED + ChatColor.STRIKETHROUGH.toString() : ChatColor.GREEN.toString();
            Answer answer = Answer.of(crossed + level.getName());
            answer.addAction(Action.setConversationVariable("city_upgrade_level", level.getId()));
            answer.addAction(Action.setConversationVariable("city_upgrade_level_name", level.getName()));
            answer.addAction(Action.changeStage(config.getString("next-stage", "next")));
            stage.addAnswer(answer);
        }

        stage.changeTo();
    }
}
