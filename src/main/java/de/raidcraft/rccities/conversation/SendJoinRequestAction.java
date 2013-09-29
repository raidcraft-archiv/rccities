package de.raidcraft.rccities.conversation;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.resident.RolePermission;
import de.raidcraft.rcconversations.api.action.AbstractAction;
import de.raidcraft.rcconversations.api.action.ActionArgumentList;
import de.raidcraft.rcconversations.api.action.ActionInformation;
import de.raidcraft.rcconversations.api.action.WrongArgumentValueException;
import de.raidcraft.rcconversations.api.conversation.Conversation;
import de.raidcraft.rcconversations.util.ParseString;

/**
 * @author Philip Urban
 */
@ActionInformation(name="SEND_CITY_JOIN_REQUEST")
public class SendJoinRequestAction extends AbstractAction {

    @Override
    public void run(Conversation conversation, ActionArgumentList args) throws RaidCraftException {

        String cityName = args.getString("city");
        cityName = ParseString.INST.parse(conversation, cityName);

        City city = RaidCraft.getComponent(RCCitiesPlugin.class).getCityManager().getCity(cityName);
        if(city == null) {
            throw new WrongArgumentValueException("Wrong argument value in action '" + getName() + "': City '" + cityName + "' does not exist!");
        }

        city.sendJoinRequest(conversation.getPlayer().getName());
        RaidCraft.getComponent(RCCitiesPlugin.class).getResidentManager()
                .broadcastCityMessage(city, conversation.getPlayer().getName() + " möchte gerne der Gilde beitreten!", RolePermission.STAFF);

        // delete all other join requests
        RaidCraft.getComponent(RCCitiesPlugin.class).getResidentManager().deleteOtherJoinRequests(conversation.getPlayer().getName(), city);
    }
}
