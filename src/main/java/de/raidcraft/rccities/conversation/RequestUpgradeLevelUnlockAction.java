package de.raidcraft.rccities.conversation;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.request.UpgradeRequest;
import de.raidcraft.rcconversations.api.action.AbstractAction;
import de.raidcraft.rcconversations.api.action.ActionArgumentList;
import de.raidcraft.rcconversations.api.action.ActionInformation;
import de.raidcraft.rcconversations.api.action.WrongArgumentValueException;
import de.raidcraft.rcconversations.api.conversation.Conversation;
import de.raidcraft.rcconversations.conversations.EndReason;
import de.raidcraft.rcconversations.util.MathHelper;
import de.raidcraft.rcconversations.util.ParseString;
import de.raidcraft.rcupgrades.api.level.UpgradeLevel;
import de.raidcraft.rcupgrades.api.unlockresult.UnlockResult;
import de.raidcraft.rcupgrades.api.upgrade.Upgrade;
import de.raidcraft.util.DateUtil;
import org.bukkit.ChatColor;

/**
 * @author Philip Urban
 */
@ActionInformation(name="REQUEST_UPRAGE_UNLOCK")
public class RequestUpgradeLevelUnlockAction extends AbstractAction {

    @Override
    public void run(Conversation conversation, ActionArgumentList args) throws RaidCraftException {

        String cityName = args.getString("city");
        cityName = ParseString.INST.parse(conversation, cityName);
        String upgradeType = args.getString("upgrade-type");
        upgradeType = ParseString.INST.parse(conversation, upgradeType);
        String upgradeLevelString = args.getString("upgrade-level");
        upgradeLevelString = ParseString.INST.parse(conversation, upgradeLevelString);
        int upgradeLevel = MathHelper.solveIntegerFormula(upgradeLevelString);

        City city = RaidCraft.getComponent(RCCitiesPlugin.class).getCityManager().getCity(cityName);
        if(city == null) {
            throw new WrongArgumentValueException("Wrong argument value in action '" + getName() + "': City '" + cityName + "' does not exist!");
        }

        Upgrade upgrade = city.getUpgrades().getUpgrade(upgradeType);
        if(upgrade == null) {
            throw new WrongArgumentValueException("Wrong argument value in action '" + getName() + "': Upgrade '" + upgradeType + "' does not exist!");
        }

        UpgradeLevel<City> level = upgrade.getLevel(upgradeLevel);
        if(level == null) {
            throw new WrongArgumentValueException("Wrong argument value in action '" + getName() + "': Level '" + upgradeLevel + "' does not exist!");
        }

        //TODO implement

        // level is already unlocked
        if(level.isUnlocked()) {
            conversation.getPlayer().sendMessage(ChatColor.RED + "Dieses Upgrade ist bereits freigeschaltet!");
            conversation.endConversation(EndReason.INFORM);
        }

        UpgradeRequest upgradeRequest = RaidCraft.getComponent(RCCitiesPlugin.class).getCityManager().getUpgradeRequest(city, level);
        conversation.getPlayer().sendMessage("");

        // check existing request
        if(upgradeRequest != null) {
            // check if rejected (cooldown)
            if(upgradeRequest.isRejected()) {
                conversation.getPlayer().sendMessage(ChatColor.RED + "Die Freischaltung wurde vor kurzem abgelehnt!");
                conversation.getPlayer().sendMessage(ChatColor.RED + "Grund: " + upgradeRequest.getRejectReason());
                conversation.getPlayer().sendMessage(ChatColor.RED + "Der nächste Antrag kann am " + DateUtil.getDateString(upgradeRequest.getRejectExpirationDate())
                        + " gestellt werden.");
                conversation.endConversation(EndReason.INFORM);
            }

            // check if in request progress
            else {
                conversation.getPlayer().sendMessage(ChatColor.RED + "Die Freischaltung wurde bereits beantragt. Bitte habe etwas Geduld!");
                conversation.endConversation(EndReason.INFORM);
            }
        }

        // add request
        UnlockResult unlockResult = level.tryToUnlock(city);

        if(unlockResult.isSuccessful()) {
            conversation.getPlayer().sendMessage(ChatColor.GREEN + "Die Freischaltung war erfolgreich!");
            conversation.endConversation(EndReason.INFORM);
        }
        else {
            conversation.getPlayer().sendMessage(ChatColor.RED + unlockResult.getLongReason());
            conversation.endConversation(EndReason.INFORM);
        }
    }
}
