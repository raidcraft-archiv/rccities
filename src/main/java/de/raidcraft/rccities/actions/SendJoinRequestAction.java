package de.raidcraft.rccities.actions;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.action.action.Action;
import de.raidcraft.api.conversations.conversation.ConversationVariable;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.flags.CityFlag;
import de.raidcraft.rccities.api.request.JoinRequest;
import de.raidcraft.rccities.api.resident.RolePermission;
import de.raidcraft.rccities.flags.city.admin.InviteCityFlag;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
public class SendJoinRequestAction implements Action<Player> {

    @Override
    @Information(
            value = "city.join-request.send",
            desc = "Sends a join request to the city for the given player.",
            conf = "city: to send join request to",
            aliases = "SEND_CITY_JOIN_REQUEST"
    )
    public void accept(Player player, ConfigurationSection config) {

        String cityName = ConversationVariable.getString(player, "city").orElse(config.getString("city"));

        City city = RaidCraft.getComponent(RCCitiesPlugin.class).getCityManager().getCity(cityName);
        if (city == null) {
            player.sendMessage(ChatColor.RED + "City " + cityName + " does not exist!");
            return;
        }

        // invitation is locked
        CityFlag inviteFlag = RaidCraft.getComponent(RCCitiesPlugin.class).getFlagManager().getCityFlag(city, InviteCityFlag.class);
        if (inviteFlag != null && !inviteFlag.getType().convertToBoolean(inviteFlag.getValue())) {
            player.sendMessage("");
            player.sendMessage(ChatColor.RED + "Diese Gilde darf zurzeit keine neuen Mitglieder aufnehmen!");
            return;
        }

        JoinRequest joinRequest = city.getJoinRequest(player.getUniqueId());
        if (joinRequest != null) {
            if (joinRequest.isRejected()) {
                player.sendMessage("");
                player.sendMessage(ChatColor.RED + "Diese Gilde will dich nicht als Mitglied haben!");
                player.sendMessage(ChatColor.RED + "Grund:" + joinRequest.getRejectReason());
                return;
            }
        }

        city.sendJoinRequest(player.getUniqueId());
        RaidCraft.getComponent(RCCitiesPlugin.class).getResidentManager()
                .broadcastCityMessage(city, player.getName()
                        + " möchte gerne der Gilde beitreten!", RolePermission.STAFF);

        // delete all other join requests
        RaidCraft.getComponent(RCCitiesPlugin.class).getResidentManager()
                .deleteOtherJoinRequests(player.getUniqueId(), city);
    }
}
