package de.raidcraft.rccities.conversation;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.api.economy.Economy;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rcconversations.api.action.AbstractAction;
import de.raidcraft.rcconversations.api.action.ActionArgumentList;
import de.raidcraft.rcconversations.api.action.ActionInformation;
import de.raidcraft.rcconversations.api.action.WrongArgumentValueException;
import de.raidcraft.rcconversations.api.conversation.Conversation;
import de.raidcraft.rcconversations.util.ParseString;
import org.bukkit.ChatColor;

/**
 * @author Philip Urban
 */
@ActionInformation(name="CITY_WITHDRAW")
public class WithdrawAction extends AbstractAction {

    @Override
    public void run(Conversation conversation, ActionArgumentList args) throws RaidCraftException {

        String cityName = args.getString("city");
        cityName = ParseString.INST.parse(conversation, cityName);
        String success = args.getString("onsuccess", null);
        String failure = args.getString("onfailure", null);
        String amountString = args.getString("amount");
        amountString = ParseString.INST.parse(conversation, amountString);

        Double amount;
        try {
            amount = Double.parseDouble(amountString);
        } catch (NumberFormatException e) {
            throw new WrongArgumentValueException("Wrong argument value in action '" + getName() + "': Amount '" + amountString + "' is not a double!");
        }

        City city = RaidCraft.getComponent(RCCitiesPlugin.class).getCityManager().getCity(cityName);
        if(city == null) {
            throw new WrongArgumentValueException("Wrong argument value in action '" + getName() + "': City '" + cityName + "' does not exist!");
        }

        if(amount == 0) {
            changeStage(conversation, failure);
            return;
        }

        Economy economy = RaidCraft.getEconomy();
        economy.substract(city.getBankAccountName(), amount);
        economy.add(conversation.getPlayer().getName(), amount);

        conversation.getPlayer().sendMessage(ChatColor.GREEN + "Du hast " + economy.getFormattedAmount(amount) + ChatColor.GREEN + " aus der Stadtkasse abgehoben!");
        RaidCraft.getComponent(RCCitiesPlugin.class).getResidentManager().broadcastCityMessage(city, conversation.getPlayer().getName()
                + " hat " + economy.getFormattedAmount(amount) + ChatColor.GOLD + " aus der Stadtkasse abgehoben!");

        changeStage(conversation, success);
    }

    private void changeStage(Conversation conversation, String stage) {

        if (stage != null) {
            conversation.setCurrentStage(stage);
            conversation.triggerCurrentStage();
        }
    }
}
