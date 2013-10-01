package de.raidcraft.rccities.conversation;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.api.economy.BalanceSource;
import de.raidcraft.api.economy.Economy;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.flags.CityFlag;
import de.raidcraft.rccities.api.request.JoinRequest;
import de.raidcraft.rccities.api.resident.RolePermission;
import de.raidcraft.rccities.flags.city.JoinCostsCityFlag;
import de.raidcraft.rcconversations.api.action.AbstractAction;
import de.raidcraft.rcconversations.api.action.ActionArgumentList;
import de.raidcraft.rcconversations.api.action.ActionInformation;
import de.raidcraft.rcconversations.api.action.WrongArgumentValueException;
import de.raidcraft.rcconversations.api.conversation.Conversation;
import de.raidcraft.rcconversations.util.ParseString;

/**
 * @author Philip Urban
 */
@ActionInformation(name="ACCEPT_CITY_JOIN_REQUEST")
public class AcceptJoinRequestAction extends AbstractAction {

    @Override
    public void run(Conversation conversation, ActionArgumentList args) throws RaidCraftException {

        String cityName = args.getString("city");
        cityName = ParseString.INST.parse(conversation, cityName);
        String candidate = args.getString("candidate");
        candidate = ParseString.INST.parse(conversation, candidate);

        City city = RaidCraft.getComponent(RCCitiesPlugin.class).getCityManager().getCity(cityName);
        if(city == null) {
            throw new WrongArgumentValueException("Wrong argument value in action '" + getName() + "': City '" + cityName + "' does not exist!");
        }

        JoinRequest joinRequest = city.getJoinRequest(candidate);
        if(joinRequest == null) return;

        Economy economy = RaidCraft.getEconomy();

        if(!economy.hasEnough(city.getBankAccountName(), RaidCraft.getComponent(RCCitiesPlugin.class).getConfig().joinCosts)) {
            RaidCraft.getComponent(RCCitiesPlugin.class).getResidentManager().broadcastCityMessage(city, "Die Gilde hat nicht genug Geld um neue Mitglieder anzunehmen!", RolePermission.STAFF);
            return;
        }

        CityFlag joinCostsCityFlag = RaidCraft.getComponent(RCCitiesPlugin.class).getFlagManager().getCityFlag(city, JoinCostsCityFlag.class);
        if(joinCostsCityFlag != null) {
            double joinCosts = joinCostsCityFlag.getType().convertToDouble(joinCostsCityFlag.getValue());
            economy.add(city.getBankAccountName(), joinCosts, BalanceSource.GUILD, "Beitrittsbeitrag von " + candidate);
            economy.substract(candidate, joinCosts, BalanceSource.GUILD, "Beitrittskosten von " + city.getFriendlyName());
        }
        economy.substract(city.getBankAccountName(), RaidCraft.getComponent(RCCitiesPlugin.class).getConfig().joinCosts, BalanceSource.GUILD, "Beitrittssteuern von " + candidate);

        joinRequest.accept();
    }
}
