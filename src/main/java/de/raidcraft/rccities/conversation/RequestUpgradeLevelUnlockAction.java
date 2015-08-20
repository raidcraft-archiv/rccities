package de.raidcraft.rccities.conversation;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.action.action.Action;
import de.raidcraft.api.config.builder.ConfigGenerator;
import de.raidcraft.api.conversations.conversation.Conversation;
import de.raidcraft.api.conversations.conversation.ConversationEndReason;
import de.raidcraft.api.conversations.conversation.ConversationVariable;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.request.UpgradeRequest;
import de.raidcraft.rcupgrades.api.level.UpgradeLevel;
import de.raidcraft.rcupgrades.api.unlockresult.UnlockResult;
import de.raidcraft.rcupgrades.api.upgrade.Upgrade;
import de.raidcraft.util.DateUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Philip Urban
 */
public class RequestUpgradeLevelUnlockAction implements Action<Conversation> {

    @Override
    @ConfigGenerator.Information(
            value = "city.request-upgrade-unlock",
            desc = "Requests the given upgrade with the given level.",
            conf = {
                    "city: to request unlock for",
                    "city_upgrade_type",
                    "city_upgrade_level"
            },
            aliases = "REQUEST_UPRAGE_UNLOCK"
    )
    @SuppressWarnings("unchecked")
    public void accept(Conversation conversation, ConfigurationSection config) {

        if (!(conversation instanceof CityConversation)) {
            conversation.sendMessage(ChatColor.RED + "Conversation must be of type city conversation to list join requests.");
            conversation.abort(ConversationEndReason.ERROR);
            return;
        }

        City city = ((CityConversation) conversation).getCity();

        String upgradeType = ConversationVariable.getString(conversation.getOwner(), "city_upgrade_type")
                .orElse(config.getString("city_upgrade_type"));
        String upgradeLevel = ConversationVariable.getString(conversation.getOwner(), "city_upgrade_level")
                .orElse(config.getString("city_upgrade_level"));

        Upgrade upgrade = city.getUpgrades().getUpgrade(upgradeType);
        if (upgrade == null) {
            conversation.sendMessage(ChatColor.RED + "Upgrade Type '" + upgradeType + "' does not exist!");
            conversation.abort(ConversationEndReason.ERROR);
            return;
        }

        UpgradeLevel<City> level = upgrade.getLevel(upgradeLevel);
        if (level == null) {
            conversation.sendMessage(ChatColor.RED + "Upgrade Level '" + upgradeLevel + "' does not exist!");
            conversation.abort(ConversationEndReason.ERROR);
            return;
        }

        UpgradeRequest upgradeRequest = RaidCraft.getComponent(RCCitiesPlugin.class).getUpgradeRequestManager().getRequest(city, level);
        conversation.sendMessage("");

        // level is already unlocked
        if (level.isUnlocked() && level.isStored()) {
            conversation.sendMessage(ChatColor.RED + "Dieses Upgrade ist bereits freigeschaltet!");
            conversation.end(ConversationEndReason.ENDED);
            return;
        }

        // check existing request
        if (upgradeRequest != null) {
            // check if rejected (cooldown)
            if (upgradeRequest.isRejected() && System.currentTimeMillis() < upgradeRequest.getRejectExpirationDate()) {
                // check if cooldown over
                if (System.currentTimeMillis() < upgradeRequest.getRejectExpirationDate()) {
                    conversation.sendMessage(ChatColor.RED + "Die Freischaltung wurde vor kurzem abgelehnt!");
                    conversation.sendMessage(ChatColor.RED + "Grund: " + upgradeRequest.getRejectReason());
                    conversation.sendMessage(ChatColor.RED + "Der nächste Antrag kann am " + DateUtil.getDateString(upgradeRequest.getRejectExpirationDate())
                            + " gestellt werden.");
                    conversation.end(ConversationEndReason.ENDED);
                    return;
                }
            }
            // check if in request progress
            else if (!upgradeRequest.isRejected()) {
                conversation.sendMessage(ChatColor.RED + "Die Freischaltung wurde bereits beantragt.");
                conversation.end(ConversationEndReason.ENDED);
                return;
            }
            // check if the reject expiration date is in the past and reactivate the upgrade request
            else if (upgradeRequest.isRejected() && System.currentTimeMillis() > upgradeRequest.getRejectExpirationDate()) {
                conversation.sendMessage(ChatColor.RED + "Die Freischaltung wurde noch einmal beantragt.");
                upgradeRequest.reactivate();
                return;
            }
        }

        // add request
        UnlockResult unlockResult = level.tryToUnlock(city);

        if (unlockResult.isSuccessful()) {
            conversation.sendMessage(ChatColor.GREEN + "Die Freischaltung war erfolgreich!");
            conversation.end(ConversationEndReason.ENDED);
        } else {
            conversation.sendMessage(ChatColor.RED + unlockResult.getLongReason());
            conversation.end(ConversationEndReason.ENDED);
        }
    }
}
