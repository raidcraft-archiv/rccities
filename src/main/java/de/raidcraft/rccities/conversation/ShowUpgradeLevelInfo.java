package de.raidcraft.rccities.conversation;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.action.action.Action;
import de.raidcraft.api.config.builder.ConfigGenerator;
import de.raidcraft.api.conversations.conversation.Conversation;
import de.raidcraft.api.conversations.conversation.ConversationEndReason;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.request.UpgradeRequest;
import de.raidcraft.rcupgrades.api.level.UpgradeLevel;
import de.raidcraft.rcupgrades.api.upgrade.Upgrade;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Philip Urban
 */
public class ShowUpgradeLevelInfo implements Action<Conversation> {

    @ConfigGenerator.Information(
            value = "city.show-upgrade-info",
            desc = "Shows information about the given upgrade.",
            conf = {
                    "text: to display",
                    "next-stage: stage to process selected upgrade type with"
            },
            aliases = "SHOW_UPGRADE_INFO"
    )
    @SuppressWarnings("unchecked")
    @Override
    public void accept(Conversation conversation, ConfigurationSection config) {

        if (!(conversation instanceof CityConversation)) {
            conversation.sendMessage(ChatColor.RED + "Conversation must be of type city conversation to list join requests.");
            conversation.abort(ConversationEndReason.ERROR);
            return;
        }

        City city = ((CityConversation) conversation).getCity();

        String upgradeType = config.getString("city_upgrade_type");
        String upgradeLevel = config.getString("city_upgrade_level");

        Upgrade upgrade = city.getUpgrades().getUpgrade(upgradeType);
        if (upgrade == null) {
            conversation.sendMessage(ChatColor.RED + "Upgrade '" + upgradeType + "' does not exist!");
            conversation.abort(ConversationEndReason.ERROR);
            return;
        }

        UpgradeLevel<City> level = upgrade.getLevel(upgradeLevel);
        if (level == null) {
            conversation.sendMessage(ChatColor.RED + "Upgrade Level '" + upgradeLevel + "' does not exist!");
            conversation.abort(ConversationEndReason.ERROR);
            return;
        }

        String state;
        UpgradeRequest upgradeRequest = RaidCraft.getComponent(RCCitiesPlugin.class).getUpgradeRequestManager().getRequest(city, level);

        if (!level.isStored()) {
            state = ChatColor.GRAY + "Beliebig oft kaufbar!";
            conversation.set("level_state_indicator", "");
        } else if (level.isUnlocked()) {
            state = ChatColor.GREEN + "Freigeschaltet";
            conversation.set("level_state_indicator", "&c&m");
        } else if (upgradeRequest == null) {
            state = ChatColor.RED + "Nicht Freigeschaltet";
            conversation.set("level_state_indicator", "");
        } else if (upgradeRequest.isRejected()) {
            state = ChatColor.RED + "Freischaltung abgelehnt (" + upgradeRequest.getRejectReason() + ")";
            conversation.set("level_state_indicator", "");
        } else {
            state = ChatColor.YELLOW + "Freischaltung beantragt";
            conversation.set("level_state_indicator", "&c&m");
        }

        conversation.sendMessage(" ");
        conversation.sendMessage(ChatColor.AQUA + "Informationen zum Upgrade '" + ChatColor.GOLD + level.getName() + ChatColor.AQUA + "':");
        conversation.sendMessage(ChatColor.AQUA + "Status: " + state);
        conversation.sendMessage(ChatColor.AQUA + "Freischalt-Bedingung:");
        for (String requirement : level.getRequirementDescription()) {
            conversation.sendMessage(ChatColor.GRAY + "- " + ChatColor.YELLOW + requirement);
        }
        conversation.sendMessage(ChatColor.AQUA + "Belohnung:");
        for (String reward : level.getRewardDescription()) {
            conversation.sendMessage(ChatColor.GRAY + "- " + ChatColor.YELLOW + reward);
        }
    }
}
